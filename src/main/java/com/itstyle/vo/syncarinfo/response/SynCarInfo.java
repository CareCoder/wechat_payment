package com.itstyle.vo.syncarinfo.response;

import java.util.List;

public class SynCarInfo {
    public List<MonlyCarInfo> monlyCarInfo;//所有的月租车数组信息列表，如果没有，则可以选择不下发这个Json字段

    public List<BlackListVehicle> blackListVehicle;//可以免费进入的车辆

    public List<FreeVehicle> freeVehicle;//黑名单上不允许进入的车辆
}
