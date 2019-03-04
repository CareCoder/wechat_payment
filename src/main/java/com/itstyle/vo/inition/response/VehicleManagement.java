package com.itstyle.vo.inition.response;

import com.itstyle.domain.car.manager.Fastigium;
import com.itstyle.vo.syncarinfo.response.BlackListVehicle;
import com.itstyle.vo.syncarinfo.response.FreeVehicle;

import java.util.List;

public class VehicleManagement {
    public String specialCar;//包含以上关键字字的车辆可以免费进入(如：警；WJ；民航),之间以“；”进行连接

    public List<Fastigium> peakVehicle;

    public void setPeakVehicle(List<Fastigium> peakVehicle) {
        this.peakVehicle = peakVehicle;
    }
    public List<FreeVehicle>  freeVehicle;
    public List<BlackListVehicle> blackVehicle;
}
