package com.itstyle.vo.inition.response;

public class ParkingSetup {
    public String parkingName;//当前停车场的名字

    public Integer remainingParkingNum;//当前停车场剩余的车位数

    public Boolean isPrefabricationCharge;//是否预制车牌收费 ,true表示同一辆车在24h内进出多次达到最高收费标准后，后续的进出不再收费

    public Boolean unlicensedHandle;//无入场记录车是否按首段计费 还是联系客服人员 true表示自动按首段计费，false为联系客服人员 (11.5新加的字段)

    public Integer monthlyCarsOverDays;// 月卡车到期后还可以允许免费进入的天数(11.5新加的字段)

    public Boolean fixedParkingSpace;//固定车辆进出不消耗车位，不受车位满位限制(11.5新加的字段)

    public Boolean isAccurateMatching;//	车牌是否需要精确匹配，true: 全部都要对应   false 就允许错第一个汉字(11.5新加的字段)
}
