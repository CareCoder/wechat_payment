package com.itstyle.vo.IncrementMonly.response;

public class MonlyCarRenewInfo {
    public String plateID;//	车牌号码

    public String monlyCardTimeStart;//	时间格式： yyyy/MM/dd HH:mm:ss

    public String monlyCardTimeEnd;//	时间格式： yyyy/MM/dd HH:mm:ss

    public String monlyCardTimeRenew;//	时间格式： yyyy/MM/dd HH:mm:ss(注意这里在保存的时候是时间是累加的字符串，但这里只下发最新的续费时间)

    public String monlyCarPlateColor;//	月卡车辆车牌颜色(蓝/黄/黑)
}
