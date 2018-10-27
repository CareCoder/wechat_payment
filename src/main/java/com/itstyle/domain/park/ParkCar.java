package com.itstyle.domain.park;

import com.itstyle.domain.park.enums.ParkCarStatus;

public class ParkCar {
    public String openId;
    public String mcNo;
    public String carNo;
    public ParkCarStatus status;
    public Long operTime;
    public Long enterTime;
    public Integer fee;
}
