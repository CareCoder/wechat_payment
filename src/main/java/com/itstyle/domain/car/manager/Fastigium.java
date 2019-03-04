package com.itstyle.domain.car.manager;

import lombok.Data;


@Data
public class Fastigium {
    private String channelName;//通道名称
    private String ip;//对应通道IP
    private String startTime;//开始时间
    private String endTime;//结束时间
    private boolean status = false;


}
