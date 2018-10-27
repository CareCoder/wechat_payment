package com.itstyle.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.itstyle.domain.park.ParkCarOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itstyle.common.YstCommon;

/**
 * 微信工具类
 * 
 * @author Administrator
 *
 */
public class WxPayUtil {
	private static Logger log = LoggerFactory.getLogger(WxPayUtil.class);

	/**
	 * 根据code获取openid
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> getOpenIdByCode(String code) throws IOException {
		// 请求该链接获取access_token
		HttpPost httppost = new HttpPost("https://api.weixin.qq.com/sns/oauth2/access_token");
		// 组装请求参数
		String reqEntityStr = "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		reqEntityStr = reqEntityStr.replace("APPID", YstCommon.APPID);
		reqEntityStr = reqEntityStr.replace("SECRET", YstCommon.APP_SECRET);
		reqEntityStr = reqEntityStr.replace("CODE", code);
		StringEntity reqEntity = new StringEntity(reqEntityStr);
		// 设置参数
		httppost.setEntity(reqEntity);
		// 设置浏览器
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 发起请求
		CloseableHttpResponse response = httpclient.execute(httppost);
		// 获得请求内容
		String strResult = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
		// 获取openid
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = JSON.parseObject(strResult);
			map.put("openid", jsonObject.get("openid"));
			map.put("access_token", jsonObject.get("access_token"));
			map.put("refresh_token", jsonObject.get("refresh_token"));
			return map;
		} catch (Exception e) {
			log.error("获取微信openid出错{}", e);
			return null;
		}

	}

	/**
	 * 统一下单
	 * 
	 * @param body
	 * @param out_trade_no
	 * @param total_fee
	 * @param IP
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	public static String unifiedOrder(String body, String out_trade_no, Integer total_fee, String IP, String openid)
			throws Exception {
		// 设置访问路径
		HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/pay/unifiedorder");

		String nonce_str = getNonceStr().toUpperCase();// 随机
		// 组装请求参数,按照ASCII排序
		String sign = "appid=" + YstCommon.APPID + "&body=" + body + "&mch_id=" + YstCommon.MCH_ID + "&nonce_str="
				+ nonce_str + "&notify_url=" + YstCommon.NOTIFY_URL + "&openid=" + openid + "&out_trade_no="
				+ out_trade_no + "&spbill_create_ip=" + IP + "&total_fee=" + total_fee.toString() + "&trade_type="
				+ YstCommon.TRADE_TYPE_JS + "&key=" + YstCommon.KEY;// 这个字段是用于之后MD5加密的，字段要按照ascii码顺序排序
		sign = Md5Util.getMD5(sign).toUpperCase();

		// 组装包含openid用于请求统一下单返回结果的XML
		StringBuilder sb = new StringBuilder("");
		sb.append("<xml>");
		setXmlKV(sb, "appid", YstCommon.APPID);
		setXmlKV(sb, "body", body);
		setXmlKV(sb, "mch_id", YstCommon.MCH_ID);
		setXmlKV(sb, "nonce_str", nonce_str);
		setXmlKV(sb, "notify_url", YstCommon.NOTIFY_URL);
		setXmlKV(sb, "openid", openid);
		setXmlKV(sb, "out_trade_no", out_trade_no);
		setXmlKV(sb, "spbill_create_ip", IP);
		setXmlKV(sb, "total_fee", total_fee.toString());
		setXmlKV(sb, "trade_type", YstCommon.TRADE_TYPE_JS);
		setXmlKV(sb, "sign", sign);
		sb.append("</xml>");
		log.info("统一下单请求：" + sb);

		StringEntity reqEntity = new StringEntity(new String(sb.toString().getBytes("UTF-8"), "ISO8859-1"));// 这个处理是为了防止传中文的时候出现签名错误
		httppost.setEntity(reqEntity);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httppost);
		String strResult = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
		log.info("统一下单返回xml：" + strResult);
		return strResult;
	}

	/**
	 * 根据统一下单返回预支付订单的id和其他信息生成签名并拼装为map（调用微信支付）
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getPaySign(String prepay_id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String timeStamp = String.valueOf((System.currentTimeMillis() / 1000));// 1970年到现在的秒数
		String nonceStr = getNonceStr().toUpperCase();// 随机数据字符串
		String packageStr = "prepay_id=" + prepay_id;
		String signType = "MD5";
		String paySign = "appId=" + YstCommon.APPID + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id
				+ "&signType=" + signType + "&timeStamp=" + timeStamp + "&key=" + YstCommon.KEY;// 注意这里的参数要根据ASCII码 排序
		paySign = Md5Util.getMD5(paySign).toUpperCase();// 将数据MD5加密
		map.put("appId", YstCommon.APPID);
		map.put("timeStamp", timeStamp);
		map.put("nonceStr", nonceStr);
		map.put("packageStr", packageStr);
		map.put("signType", signType);
		map.put("paySign", paySign);
		map.put("prepay_id", prepay_id);
		return map;
	}

	/**
	 * 修改订单状态，获取微信回调结果
	 * 
	 * @return 如果成功返回openid ，否则返回null
	 */
	public static ParkCarOrder getNotifyResult(HttpServletRequest request) throws Exception {
		ParkCarOrder order = new ParkCarOrder();
		String notifyXml = getNotifyXml(request);

		String appid = getXmlPara(notifyXml, "appid");
		String bank_type = getXmlPara(notifyXml, "bank_type");
		String cash_fee = getXmlPara(notifyXml, "cash_fee");
		String fee_type = getXmlPara(notifyXml, "fee_type");
		String is_subscribe = getXmlPara(notifyXml, "is_subscribe");
		String mch_id = getXmlPara(notifyXml, "mch_id");
		String nonce_str = getXmlPara(notifyXml, "nonce_str");
		String openid = getXmlPara(notifyXml, "openid");
		String out_trade_no = getXmlPara(notifyXml, "out_trade_no");
		String result_code = getXmlPara(notifyXml, "result_code");
		String return_code = getXmlPara(notifyXml, "return_code");
		String sign = getXmlPara(notifyXml, "sign");
		String time_end = getXmlPara(notifyXml, "time_end");
		String total_fee = getXmlPara(notifyXml, "total_fee");
		String trade_type = getXmlPara(notifyXml, "trade_type");
		String transaction_id = getXmlPara(notifyXml, "transaction_id");

		// 根据返回xml计算本地签名
		String localSign = "appid=" + appid + "&bank_type=" + bank_type + "&cash_fee=" + cash_fee + "&fee_type="
				+ fee_type + "&is_subscribe=" + is_subscribe + "&mch_id=" + mch_id + "&nonce_str=" + nonce_str
				+ "&openid=" + openid + "&out_trade_no=" + out_trade_no + "&result_code=" + result_code
				+ "&return_code=" + return_code + "&time_end=" + time_end + "&total_fee=" + total_fee + "&trade_type="
				+ trade_type + "&transaction_id=" + transaction_id + "&key=" + YstCommon.KEY;// 注意这里的参数要根据ASCII码 排序
		localSign = Md5Util.getMD5(localSign).toUpperCase();// 将数据MD5加密

		log.debug("本地签名是：" + localSign);
		log.debug("本地签名是：" + localSign);
		log.debug("微信支付签名是：" + sign);

		// 本地计算签名与微信返回签名不同||返回结果为不成功
		if (!sign.equals(localSign) || !"SUCCESS".equals(result_code) || !"SUCCESS".equals(return_code)) {
			System.out.println("验证签名失败或返回错误结果码");
			log.error("验证签名失败或返回错误结果码");
			return null;
		} else {
			System.out.println("支付成功");
			log.debug("公众号支付成功，out_trade_no(订单号)为：" + out_trade_no);
			order.openId = openid;
			order.orderNo = out_trade_no;
			return order;
		}
	}

	public static String getNotifyXml(HttpServletRequest request) {
		String inputLine;
		String notifyXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notifyXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			log.debug("xml获取失败：" + e);
			e.printStackTrace();
		}
		System.out.println("接收到的xml：" + notifyXml);
		log.debug("收到微信异步回调：");
		log.debug(notifyXml);
		if (StringUtils.isEmpty(notifyXml)) {
			log.debug("xml为空：");
		}
		return notifyXml;
	}

	/**
	 * 获取32位随机字符串
	 * 
	 * @return
	 */
	public static String getNonceStr() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random rd = new Random();
		for (int i = 0; i < 32; i++) {
			sb.append(str.charAt(rd.nextInt(str.length())));
		}
		return sb.toString();
	}

	/**
	 * 插入XML标签
	 * 
	 * @param sb
	 * @param Key
	 * @param value
	 * @return
	 */
	public static StringBuilder setXmlKV(StringBuilder sb, String Key, String value) {
		sb.append("<");
		sb.append(Key);
		sb.append(">");

		sb.append(value);

		sb.append("</");
		sb.append(Key);
		sb.append(">");

		return sb;
	}

	/**
	 * 解析XML 获得名称为para的参数值
	 * 
	 * @param xml
	 * @param para
	 * @return
	 */
	public static String getXmlPara(String xml, String para) {
		int start = xml.indexOf("<" + para + ">");
		int end = xml.indexOf("</" + para + ">");

		if (start < 0 && end < 0) {
			return null;
		}
		return xml.substring(start + ("<" + para + ">").length(), end).replace("<![CDATA[", "").replace("]]>", "");
	}

	/**
	 * 获取ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return request.getRemoteAddr();
		}
		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		return ipAddrStr;
	}
}
