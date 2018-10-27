package com.itstyle.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.park.ParkCarOrder;
import com.itstyle.domain.templete.TemplateData;
import com.itstyle.domain.templete.TemplateMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TemplateUtils {
    private static Logger logger = LoggerFactory.getLogger(TemplateUtils.class);
    /**
     * 创建模板
     * @param order
     */
    private static Map<String, TemplateData> createOrderEmplate(ParkCarOrder order, boolean done) {
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("请您核对停车费用");
        keyword1.setColor("#000");

        TemplateData keyword2 = new TemplateData();
        keyword2.setValue(StringUtils.isEmpty(order.carNo) ? "无车牌信息" : order.carNo);
        keyword2.setColor("#000");

        TemplateData keyword3 = new TemplateData();
        keyword3.setValue("华睿智兴");
        keyword3.setColor("#000");

        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        String format = sdf.format(new Date(order.enterTime));
        TemplateData keyword4 = new TemplateData();
        keyword4.setValue(format);
        keyword4.setColor("#000");

        TemplateData keyword5 = new TemplateData();
        keyword5.setValue(FeeUtil.secondToTime(order.operTime));
        keyword5.setColor("#000");

        TemplateData keyword6 = new TemplateData();
        double fee = (double)order.fee / 100;
        keyword6.setValue(Objects.isNull(order.fee) ? "正在计算中......" : String.valueOf(fee) + "元");
        keyword6.setColor("#000");

        TemplateData keyword7 = new TemplateData();
        keyword7.setValue(".........点击支付... ......");
        if (done) {
            keyword7.setValue(".........支付完成... ......");
        }
        keyword7.setColor("#0000FF");


        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        data.put("first", keyword1);
        data.put("keyword1", keyword2);
        data.put("keyword2", keyword3);
        data.put("keyword3", keyword4);
        data.put("keyword4", keyword5);
        data.put("keyword5", keyword6);
        data.put("remark", keyword7);
        return data;
    }

    /**
     * 生成订单，发送模板
     */
    public static String createOrder(ParkCarOrder order, String token, boolean done) {
        String url = "http://" + YstCommon.SERVER + "/pay.html";
        if (done) {
            url = "";
        }
        TemplateMsg message = new TemplateMsg(YstCommon.ORDER_TEMPLATE_ID, order.openId,  url,
                "black", createOrderEmplate(order, done));
        String strResult = HttpUtils.HttPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                + token, JSON.toJSONString(message));
        // 需要解析状态
        logger.info("发送订单模板返回的消息" + strResult);
        JSONObject parseXmlStr = JSON.parseObject(strResult);
        if ("ok".equals(parseXmlStr.getString("errmsg"))) {
            return "订单生成成功";
        }
        return "订单生成失败";
    }

    /**
     * 创建模板
     */
    private static Map<String, TemplateData> createUploadInfoEmplate() {
        TemplateData keyword1 = new TemplateData();
        keyword1.setValue("---请点击输入车牌---");
        keyword1.setColor("#000");

        TemplateData keyword2 = new TemplateData();
        keyword2.setValue("无人自助停车");
        keyword2.setColor("#000");

        TemplateData keyword3 = new TemplateData();
        keyword3.setValue("车牌信息");
        keyword3.setColor("#000");

        TemplateData keyword4 = new TemplateData();
        keyword4.setValue("完成缴费后，请在15分钟内离场，如超时将需要补交费用");
        keyword4.setColor("#000");

        TemplateData keyword5 = new TemplateData();
        keyword5.setValue("谢谢您的配合");
        keyword5.setColor("#000");
        Map<String, TemplateData> data = new HashMap<String, TemplateData>();
        data.put("first", keyword1);
        data.put("keyword1", keyword2);
        data.put("keyword2", keyword3);
        data.put("keyword3", keyword4);
        data.put("remark", keyword5);
        return data;
    }

    /**
     * 生成上传信息，发送模板
     */
    public static String createUploadInfo(String fromUserName, String token) {
        String url = "http://" + YstCommon.SERVER + "/park/uploadCarInfo.html?openId=" + fromUserName;
        TemplateMsg message = new TemplateMsg(YstCommon.UPLOAD_INFO_TEMPLATE_ID, fromUserName, url,
                "black", createUploadInfoEmplate());
        String strResult = HttpUtils.HttPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                + token, JSON.toJSONString(message));
        // 需要解析状态
        logger.info("发送上传信息模板返回的消息" + strResult);
        JSONObject parseXmlStr = JSON.parseObject(strResult);
        if ("ok".equals(parseXmlStr.getString("errmsg"))) {
            return "上传信息生成成功";
        }
        return "上传信息生成失败";

    }
}
