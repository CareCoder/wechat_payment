package com.itstyle.vo.inition.response;

import com.itstyle.domain.car.manager.Fastigium;

public class ForbidenVehicle {

    public String entry;//入口
    public String entryForbidenTimeStart;//	车辆禁止进入的开始时间

    public String entryForbidenTimeEnd;//车辆禁止进入的结束时间

    public boolean entryForbidenVehicle;//true表示不允许临时车在特定时间段内进入，false表示没有限制
    public String exit;//出口
    public String exitForbidenTimeStart;//	车辆禁止出行的开始时间
    public String exitForbidenTimeEnd;//	车辆禁止出行开始时间
    public boolean exitForbidenVehicle;//true表示不允许临时车在特定时间段内出行，false表示没有限制
    public String specialCar;//包含以上关键字字的车辆可以免费进入(如：警；WJ；民航),之间以“；”进行连接

    public void setFastigium(Fastigium fastigium) {
        if (fastigium == null) {
            return;
        }
        entry = fastigium.getEntry();
        entryForbidenTimeStart = fastigium.getEntryStartTime();
        entryForbidenTimeEnd = fastigium.getEntryEndTime();
        entryForbidenVehicle = fastigium.isEntryStatus();
        exit = fastigium.getExit();
        exitForbidenTimeStart = fastigium.getExitStartTime();
        exitForbidenTimeEnd = fastigium.getExitEndTime();
        exitForbidenVehicle = fastigium.isExitStatus();
    }
}
