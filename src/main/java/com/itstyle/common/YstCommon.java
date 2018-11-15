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
	public static String APPID = "wxeae418507d27aeeb";
	// 第三方用户唯一凭证密码
	public static String APP_SECRET = "4736f21c4eec49efe9bce00f249f82f9";
	// 商户ID
	public static String MCH_ID = "1271069001";
	// 微信商户平台-账户设置-安全设置-api安全,配置32位key
	public static String KEY = "hrzx1818hrzx1818hrzx1818hrzx1818";
	// 交易类型
	public static String TRADE_TYPE_JS = "JSAPI";
	public static String SERVER = "isparking.cn";
	// 微信支付回调url
	public static String NOTIFY_URL = "http://" + SERVER + "/wx/pay_notify";
	public static String wechat_token = "test";

	//系统常用常量
	public static String INNER_MC_NO = "000";

	/**模版消息id*/
	//停车缴费提醒
	public static String ORDER_TEMPLATE_ID = "T0h-ClnbHr_yBdFf6JIo6KR0Z0F9WfV-JpDTEPZE0-8";
	//完善资料通知
	public static String UPLOAD_INFO_TEMPLATE_ID = "F7Ij0kuiPBkrD4OrENzONw4uUifvwUTD_n4nQm7EIjE";
	// 登陆成功，session中存储的属性名
	public static String LOGIN_ACCOUNT = "login_account";


	/**
	 * redis的field和key
	 */
	public static final String GLOBAL_SETTIING_FIELD = "GLOBAL_SETTIING_FIELD";
	public static final String FASTIGIUM_KEY = "FASTIGIUM_KEY";
    public static final String BANLISTMANAGER_KEY = "BANLISTMANAGER_KEY";
	public static final String FIXEDCARMANAGER_KEY = "FIXEDCARMANAGER_KEY";
	/** 车场名称设置 */
	public static final String CAR_YARD_NAME = "CAR_YARD_NAME";
	public static final String EQUIPMENT_STATUS = "EQUIPMENT_STATUS";

	/** 深圳收费 */
	public static final String SZ_CHARGES = "SC_CHARGES";
	/** 标准收费 */
	public static final String STANDARD_CHARGES = "STANDARD_CHARGES";
	/** 按次收费 */
	public static final String BY_CHARGES = "BY_CHARGES";
}
