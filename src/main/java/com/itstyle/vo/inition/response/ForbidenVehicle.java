package com.itstyle.vo.inition.response;

import com.itstyle.domain.car.manager.Fastigium;

import java.util.List;

public class ForbidenVehicle {


    public String specialCar;//包含以上关键字字的车辆可以免费进入(如：警；WJ；民航),之间以“；”进行连接

    public List<Fastigium> peakVehicle;

    public void setPeakVehicle(List<Fastigium> peakVehicle) {
        this.peakVehicle = peakVehicle;
    }
}
