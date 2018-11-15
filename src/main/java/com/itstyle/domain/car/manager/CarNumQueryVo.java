package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarType;
import lombok.Data;

@Data
public class CarNumQueryVo {
    private int page;
    private int limit;
    private String carNum;
    private CarType carType;
    private Long startTime;
    private Long endTime;
}
