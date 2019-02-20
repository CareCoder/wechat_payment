package com.itstyle.control;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itstyle.domain.park.ParkCarOrder;
import com.itstyle.service.ParkCarService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itstyle.common.YstCommon;
import com.itstyle.task.AssessTokenTask;
import com.itstyle.utils.HttpUtils;
import com.itstyle.utils.WxPayUtil;
import com.itstyle.utils.XmlUtils;

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

}
