package com.itstyle.domain.park;

import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.domain.park.enums.ParkCarStatus;

public class ParkCarOrder {
    public String openId;
    public String mcNo;
    public String carNo;
    public ParkCarStatus status;
    public Long operTime;
    public Integer fee;
    public Long enterTime;
    public String orderNo;
    public CarType carType;

    public static ParkCarOrder make(ParkCar parkCar) {
        ParkCarOrder order = new ParkCarOrder();
        order.carNo = parkCar.carNo;
        order.fee = parkCar.fee;
        order.mcNo = parkCar.mcNo;
        order.openId = parkCar.openId;
        order.operTime = parkCar.operTime;
        order.status = parkCar.status;
        order.orderNo = System.currentTimeMillis() + "";
        order.enterTime = parkCar.enterTime;
        return order;
    }

    public static ParkCar convert(ParkCarOrder order) {
        ParkCar parkCar = new ParkCar();
        parkCar.carNo = order.carNo;
        parkCar.status = order.status;
        parkCar.fee = order.fee;
        parkCar.operTime = order.operTime;
        parkCar.openId = order.openId;
        parkCar.mcNo = order.mcNo;
        parkCar.enterTime = order.enterTime;
        return parkCar;
    }
}
