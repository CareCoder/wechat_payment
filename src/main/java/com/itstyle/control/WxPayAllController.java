package com.itstyle.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.*;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import com.itstyle.domain.park.ParkCarOrder;
import com.itstyle.domain.weixin.RedPackEntity;
import com.itstyle.service.ParkCarService;
import com.itstyle.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itstyle.common.YstCommon;
import com.itstyle.task.AssessTokenTask;
import org.thymeleaf.expression.Maps;

/**
 * 微信支付
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wx")
public class WxPayAllController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AssessTokenTask assessTokenTask;
	@Resource
	private ParkCarService parkCarService;

	/**
	 * 公众号支付
	 */
	@RequestMapping(value = "/gzhpay")
	public String webPay(HttpServletRequest request) {
		try {
			// 获取用户的code
			String code = request.getParameter("code");
			log.info("获取用户信息的CODE" + code);
			Map<String, Object> openIdByCode = WxPayUtil.getOpenIdByCode(code);
			if (openIdByCode == null) {
				return "/error/404";
			}
			// 获取客户端id
			String ip = WxPayUtil.getIpAddr(request);
			if (StringUtils.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			String result;
			String openid = (String)openIdByCode.get("openid");
			ParkCarOrder order = parkCarService.queryPay(openid);
			if (order == null || order.fee == null) {
				//订单已经产生了，但是订单金额还没获取到
				return "/nopay";
			}
			result = WxPayUtil.unifiedOrder("测试订单", order.orderNo + "", order.fee, ip, openid);// 创建预订单
			Map<String, Object> resultMap = XmlUtils.Dom2Map(result);
			String return_code = (String) resultMap.get("return_code");
			if (!StringUtils.equals(return_code, "SUCCESS")) {
				return "/error/404";
			}
			String result_code = (String) resultMap.get("result_code");
			if (!StringUtils.equals(result_code, "SUCCESS")) {
				return "/error/404";
			}
			// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
			String prepay_id = (String) resultMap.get("prepay_id");
			Map<String, Object> paySign = WxPayUtil.getPaySign(prepay_id);
			request.setAttribute("appid", YstCommon.APPID);// 自己设置
			request.setAttribute("timeStamp", paySign.get("timeStamp"));
			request.setAttribute("nonceStr", paySign.get("nonceStr"));
			request.setAttribute("packageStr", paySign.get("packageStr"));
			request.setAttribute("signType", paySign.get("signType"));
			request.setAttribute("paySign", paySign.get("paySign"));
			request.setAttribute("prepay_id", prepay_id);
			request.setAttribute("success", "ok");
		} catch (Exception e) {
			log.error("创建订单出错{}", e);
			return "/error/404";
		}
		return "/pay";
	}

	/**
	 * 微信回调
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay_notify", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String pay_notify(HttpServletRequest request) {
		try {
			ParkCarOrder order = WxPayUtil.getNotifyResult(request);
			if (order != null) {
				ParkCarOrder mOrder = parkCarService.queryPay(order.openId);
				if (Objects.equals(mOrder.orderNo, order.orderNo)) {
					parkCarService.done(order.openId);
				}
				return "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[FAIL]]></return_msg>" + "</xml> ";
			}else{
				return "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
						+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			}
		} catch (Exception e) {
			log.error("微信回调出错{}", e);
			return null;
		}
	}

	/**
	 * 生成二维码
	 * 
	 * @return
	 */
	@RequestMapping("/createCode")
	public String createQrcode(String args) {
		if (StringUtils.isBlank(args)) {
			return "/error/404";
		}
		try {
			String json = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+ args +"\"}}}";
			String strResult = HttpUtils.HttPost(
					"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + assessTokenTask.getAssessToken(),
					json);
			JSONObject jsonObject = JSON.parseObject(strResult);
			String ticket = (String) jsonObject.get("ticket");
			System.out.println(strResult);
			return "redirect:https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
		} catch (Exception e) {
			log.error("生成二维码报错{}", e);
			return null;
		}

	}

	/**
	 * 生成临时二维码（一天）
	 *
	 * @return
	 */
	@RequestMapping("/createTemporaryQRCode")
	public String createTemporaryQRCode(Integer args){
		if (args<=0) {
			return "/error/404";
		}
		try {
			String json = "{\"expire_seconds\": 86400,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \""+ args +"\"}}}";
			String strResult = HttpUtils.HttPost(
					"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + assessTokenTask.getAssessToken(),
					json);
			JSONObject jsonObject = JSON.parseObject(strResult);
			String ticket = (String) jsonObject.get("ticket");
			System.out.println(strResult);
			log.info("strResult:"+strResult);
			return "redirect:https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
		} catch (Exception e) {
			log.error("生成二维码报错{}", e);
			return null;
		}
	}


	@RequestMapping("/sendRedPacket")
	public String sendRedPacket(HttpServletRequest request,Integer total_amount,String re_openid) {
			log.info("获取用户信息的openid" + re_openid);
			//开始发送红包
			log.info("++++++++++++++开始发送红包++++++++++++++++++");
			Map<String, String> params = new HashMap<>();
			/** 当前时间 yyyyMMddHHmmss */
			String currTime = WxPayUtil.getCurrTime();
			/** 8位日期 */
			String strTime = currTime.substring(8, currTime.length());
			log.info("日期：" + strTime);
			/** 四位随机数 */
			String strRandom = WxPayUtil.buildRandom(4) + "";
			log.info("随机数：" + strRandom);
			/** 商户号 */
			String mch_id = YstCommon.MCH_ID;
			//商户订单号
			String mch_billno = mch_id + strTime + strRandom;
			/** 随机字符串 */
			String nonce_str = WxPayUtil.getNonceStr();
			/** 公众号APPID */
			String wxappid = YstCommon.APPID;
			/** 商户名称 */
			String send_name = "深圳市华睿智兴信息科技有限公司";
			/** 用户openid */
			/** 付款金额，红包的值，最低100分*/
			/** 红包发放总人数：1人*/
			Integer total_num = 1;
			/** 红包祝福语 */
			String wishing = "微信找零";
			/** 调用接口的机器Ip地址 */
			String client_ip = WxPayUtil.getIpAddr(request);
			if (StringUtils.isBlank(client_ip)) {
				client_ip = "127.0.0.1";
			}
			/** 活动名称 */
			String act_name = "微信找零";
			/** 备注 */
			String remark = "欢迎下次再来";

			RedPackEntity redPackEntity = new RedPackEntity(nonce_str, mch_billno, mch_id, wxappid, send_name, re_openid, total_amount, total_num, wishing, client_ip, act_name, remark);
			String sign = RedPackUtil.createRedPackOrderSign(redPackEntity);
			log.info("签名："+sign);
			redPackEntity.setSign(sign);
			XmlUtils.xstream().autodetectAnnotations(true);
			XmlUtils.xstream().alias("xml", redPackEntity.getClass());
			String data = XmlUtils.xstream().toXML(redPackEntity);
			log.info("X生成ML信息："+data);
			return RedPackUtil.sendRedPack(data);
	}
}