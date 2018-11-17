package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarType;
import lombok.Data;

@Data
public class CarNumQueryVo {
    private int page = 1;
    private int limit = 4;
    private String carNum;
    private CarType carType;
    private Long startTime;
    private Long endTime;
}