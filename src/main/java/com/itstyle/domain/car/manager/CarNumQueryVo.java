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
    private Long leaveStartTime;
    private Long leaveEndTime;
    private String leavePass;
    private String enterPass;
    private Boolean record;//这次临时停车是否已经生成明细
    private Boolean leave;//true代表进入了并且离开了,false代表还未离开
}
