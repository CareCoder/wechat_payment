package com.itstyle.vo.inition.response;

import lombok.Data;

@Data
public class TextDisplay {
    //入口
    private String tempCarEntrance;//入口语句：
    private String parkingFull;//停车位已满语句：parkingFull
    private String tempCarForbiddenEntrance;//临时车不让进场语句：tempCarForbiddenEntrance
    private String blackListForbidden;//黑名单车辆禁止入场语句：blackListForbidden

    //出口
    private String tempCarExit;//出口语句：tempCarExit
    private String alreadyPayment;//场内已完成付费的提示语句：alreadyPayment
    private String paymentOverTime;//缴费超时提示语句：paymentOverTime

    //无入场记录车牌出场异常语句(分成2个输入框，1个是默认用首段计费提示语， 1个是提示联系管理员)：
    private String exceptionExit1;//      1、exceptionExit1
    private String exceptionExit2;//2、exceptionExit2

    //设置公共语句
    private String normalMonlyCar;//正常期限月租车语句：normalMonlyCar
    private String delayMonlyCar;//超过期限月租车语句：delayMonlyCar
    private String specialCar;//特殊车辆通行语句：specialCar
    private String VIP;//VIP车辆语句：VIP
}
