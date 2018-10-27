package com.itstyle.common;

/**
 * 常量配置
 * 
 * @author Administrator
 *
 */
public class YstCommon {
	public static final String SIGNATURE = "";
	public static final String TIMESTAMP = "";
	public static final String NONCE = "";
	public static final String ECHOSTR = "";
	// 第三方用户唯一ID
	public static String APPID = "wxd49da7cb176f674b";
	// 第三方用户唯一凭证密码
	public static String APP_SECRET = "20698c3c86cf16af7845f520100705fd";
	// 商户ID
	public static String MCH_ID = "1513442631";
	// 微信商户平台-账户设置-安全设置-api安全,配置32位key
	public static String KEY = "DAIdapeng070718888871628ABCDEFGH";
	// 交易类型
	public static String TRADE_TYPE_JS = "JSAPI";
	public static String SERVER = "ltcx.isparking.cn";
	// 微信支付回调url
	public static String NOTIFY_URL = "http://" + SERVER + "/wx/pay_notify";
	public static String wechat_token = "test";

	//系统常用常量
	public static String INNER_MC_NO = "000";

	/**模版消息id*/
	//停车缴费提醒
	public static String ORDER_TEMPLATE_ID = "NDb-_mv9WgbqFnnQH-ErcgeQL3wOWLCa95gDxDIFarU";
	//完善资料通知
	public static String UPLOAD_INFO_TEMPLATE_ID = "orwLd4A5nsFVPChZVTSf75gNHyKHEhChu7JV4J-XtbI";
}
