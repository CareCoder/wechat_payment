package com.itstyle.domain.car.manager;

import lombok.Data;

@Data
public class Fastigium {
    private Long startTime;

    private Long endTime;

    private boolean status = false;

    public static Fastigium buildDefault() {
        Fastigium fastigium = new Fastigium();
        fastigium.setStartTime(0L);
        fastigium.setEndTime(0L);
        fastigium.setStatus(false);
        return fastigium;
    }

}
