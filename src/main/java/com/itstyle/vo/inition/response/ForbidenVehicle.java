package com.itstyle.vo.inition.response;

import com.itstyle.domain.car.manager.Fastigium;

public class ForbidenVehicle {
    public Long forbidenTimeStart;//	车辆禁止进入的开始时间

    public Long forbidenTimeEnd;//车辆禁止进入的结束时间

    public boolean forbidenVehicle;//true表示不允许临时车在特定时间段内进入，false表示没有限制

    public String specialCar;//包含以上关键字字的车辆可以免费进入(如：警；WJ；民航),之间以“；”进行连接

    public void setFastigium(Fastigium fastigium) {
        if (fastigium == null) {
            return;
        }
        forbidenTimeStart = fastigium.getStartTime();
        forbidenTimeEnd = fastigium.getEndTime();
        forbidenVehicle = fastigium.isStatus();
    }
}
