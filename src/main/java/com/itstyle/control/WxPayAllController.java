package com.itstyle.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import com.itstyle.domain.park.ParkCarOrder;
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
	public String sendRedPacket(HttpServletRequest request,int total_amount,String re_openid) {
		try {
			log.info("获取用户信息的openid" + re_openid);
			//开始发送红包
			log.info("++++++++++++++开始发送红包++++++++++++++++++");
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			/** 当前时间 yyyyMMddHHmmss */
			String currTime = WxPayUtil.getCurrTime();
			/** 8位日期 */
			String strTime = currTime.substring(8, currTime.length());
			/** 四位随机数 */
			String strRandom = WxPayUtil.buildRandom(4) + "";
			//商户订单号
			String mch_billno = strTime + strRandom;
			parameters.put("mch_billno", mch_billno);
			/** 商户号 */
			String mch_id = YstCommon.MCH_ID;
			parameters.put("mch_id", mch_id);
			/** 随机字符串 */
			String nonce_str = WxPayUtil.getNonceStr();
			parameters.put("nonce_str", nonce_str);
			/** 公众号APPID */
			String wxappid = YstCommon.APPID;
			parameters.put("wxappid", wxappid);
			/** 商户名称 */
			String send_name = "深圳市华睿智兴信息科技有限公司";
			parameters.put("send_name", send_name);
			/** 用户openid */
			parameters.put("re_openid", re_openid);
			/** 付款金额，红包的值，最低100分*/
			parameters.put("total_amount", total_amount);
			/** 红包发放总人数：1人*/
			int total_num = 1;
			parameters.put("total_num", total_num);
			/** 红包祝福语 */
			String wishing = "微信找零";
			parameters.put("wishing", wishing);
			/** 调用接口的机器Ip地址 */
			String client_ip = WxPayUtil.getIpAddr(request);
			if (StringUtils.isBlank(client_ip)) {
				client_ip = "127.0.0.1";
			}
			parameters.put("client_ip", client_ip);
			/** 活动名称 */
			String act_name = "微信找零";
			parameters.put("act_name", act_name);
			/** 备注 */
			String remark = "欢迎下次再来";
			parameters.put("remark", remark);
			/** 场景id  发放红包使用场景，红包金额小于1或者大于200时必传*/
			//parameters.put("scene_id", "PRODUCT_2");
			/** MD5进行签名，必须为UTF-8编码，注意上面几个参数名称的大小写 */
			// 组装请求参数,按照ASCII排序
			String sign = "act_name=" + act_name + "&client_ip=" + client_ip + "&mch_billno=" + mch_billno + "&mch_id="
					+ mch_id + "&nonce_str=" + nonce_str + "&re_openid=" + re_openid + "&remark="
					+ remark + "&send_name=" + send_name + "&total_amount=" + total_amount + "&total_num="
					+ total_num + "&wishing=" + wishing + "&wxappid=" + wxappid + "&key=" + YstCommon.KEY;// 这个字段是用于之后MD5加密的，字段要按照ascii码顺序排序
			Md5Util.getMD5(sign).toUpperCase();
			String requestJsonStr = JSON.toJSONString(parameters);
			log.info("发送的信息是" + requestJsonStr);
			parameters.put("sign", sign);//
			/** 生成xml结构的数据，用于统一下单接口的请求 */
			String requestXML = WxPayUtil.getRequestXml(parameters);
			/**
			 * 读取证书
			 */
			CloseableHttpClient httpclient = null;
			Map<String, String> result = new HashMap<String, String>();
			try {
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				//String pathname = "http://isparking.cn/certificate/";//证书的地址
				//FileInputStream instream = new FileInputStream(new File(pathname)); //证书所放的绝对路径
				ClassPathResource pathResource = new ClassPathResource("certificate/apiclient_cert.p12");
				try {
					keyStore.load(pathResource.getInputStream(), mch_id.toCharArray());
				}catch (Exception e){
					log.info("访问证书路径时发生的异常信息：" + e.getMessage());
					e.printStackTrace();
				}finally {
					//instream.close();
				}
				SSLContext sslcontext = SSLContexts.custom()
						.loadKeyMaterial(keyStore, mch_id.toCharArray())
						.build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslcontext,
						new String[]{"TLSv1"}, null
						,
						SSLConnectionSocketFactory.getDefaultHostnameVerifier());//SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
				httpclient = HttpClients.custom()
						.setSSLSocketFactory(sslsf)
						.build();
			} catch (Exception e) {
				log.info("读取证书信息的时候发生异常异常信息是：" + e.getMessage());
				e.printStackTrace();
			}

			try {
				String requestUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
				HttpPost httpPost = new HttpPost(requestUrl);
				StringEntity reqEntity = new StringEntity(requestXML, "utf-8");
				// 设置类型
				reqEntity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(reqEntity);
				log.info("executing request" + httpPost.getRequestLine());
				CloseableHttpResponse response = httpclient.execute(httpPost);
				try {
					HttpEntity entity = response.getEntity();
					System.out.println(response.getStatusLine());
					if (entity != null) {
						// 从request中取得输入流
						InputStream inputStream = entity.getContent();
						// 读取输入流
						SAXReader reader = new SAXReader();
						Document document = reader.read(inputStream);
						// 得到xml根元素
						Element root = document.getRootElement();
						// 得到根元素的所有子节点
						List<Element> elementList = root.elements();
						// 遍历所有子节点
						for (Element e : elementList) {
							result.put(e.getName(), e.getText());
						}
						// 释放资源
						inputStream.close();
					}
					EntityUtils.consume(entity);
				} finally {
					if (response != null) {
						response.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			log.info("------------------发送红包结束---------------");
			log.info("发送红包微信返回的信息是：" + JSON.toJSONString(result));
			//假如发送成功的话，保存发送的信息
			if (result.get("return_msg").equals("发放成功")) {
				log.info("红包发放成功，openid=" + re_openid + ",发送时间是：" + WxPayUtil.getPreDay(new Date(), 0));
				return "红包发放成功!";
			} else {
				log.info("红包发放失败，openid=" + re_openid + ",发送时间是：" + WxPayUtil.getPreDay(new Date(), 0));
				return "红包发放失败!";
			}
		} catch (Exception e) {
			log.info("发送红包异常，异常信息是：" + e.getMessage());
			return "发送红包时出现异常！";
		}
	}
}