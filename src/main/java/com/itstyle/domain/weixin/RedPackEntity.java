package com.itstyle.domain.weixin;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class RedPackEntity {
    /**
     * 随机字符串,不长于32位
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String sign;
    /**
     * 商户订单号
     */
    @XStreamAlias("mch_billno")
    private String mchBillNo;
    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     * 服务商号微信公众号的APPID
     */
    @XStreamAlias("wxappid")
    private String wxAppid;

    /**
     * 红包发送者名称
     */
    @XStreamAlias("send_name")
    private String sendName;
    /**
     * 接收红包的用户
     */
    @XStreamAlias("re_openid")
    private String reOpenid;
    /**
     * 付款金额,单位分
     */
    @XStreamAlias("total_amount")
    private int totalAmount;
    /**
     * 红包发放总人数
     */
    @XStreamAlias("total_num")
    private int totalNum;
    /**
     * 红包祝福语
     */
    @XStreamAlias("wishing")
    private String wishing;
    /**
     * 调用接口的机器IP地址
     */
    @XStreamAlias("client_ip")
    private String clientIp;
    /**
     * 活动名称
     */
    @XStreamAlias("act_name")
    private String actName;
    /**
     * 备注名称
     */
    @XStreamAlias("remark")
    private String remark;

    public String getNonceStr() {
        return nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public String getMchBillNo() {
        return mchBillNo;
    }

    public String getMchId() {
        return mchId;
    }



    public String getWxAppid() {
        return wxAppid;
    }



    public String getSendName() {
        return sendName;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setMchBillNo(String mchBillNo) {
        this.mchBillNo = mchBillNo;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }



    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }



    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public void setReOpenid(String reOpenid) {
        this.reOpenid = reOpenid;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReOpenid() {
        return reOpenid;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public String getWishing() {
        return wishing;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getActName() {
        return actName;
    }

    public String getRemark() {
        return remark;
    }

    /**
     * @param nonceStr    随机的字符串不能超过32位
     * @param mchBillNo   商户订单号
     * @param mchId       商户号
     * @param wxAppid     服务商号
     * @param sendName    红包发送者名称
     * @param reOpenid    接收红包的用户
     * @param totalAmount 付款金额,单位分
     * @param totalNum    红包发放总数
     * @param wishing     红包祝福语
     * @param clientIp    调用接口请求的IP
     * @param actName     活动名称
     * @param remark      备注
     */
    public RedPackEntity(String nonceStr, String mchBillNo, String mchId, String wxAppid, String sendName, String reOpenid, int totalAmount, int totalNum, String wishing, String clientIp, String actName, String remark) {
        this.nonceStr = nonceStr;
        this.mchBillNo = mchBillNo;
        this.mchId = mchId;
        this.wxAppid = wxAppid;
        this.sendName = sendName;
        this.reOpenid = reOpenid;
        this.totalAmount = totalAmount;
        this.totalNum = totalNum;
        this.wishing = wishing;
        this.clientIp = clientIp;
        this.actName = actName;
        this.remark = remark;
    }
    public RedPackEntity() {
    }
}
