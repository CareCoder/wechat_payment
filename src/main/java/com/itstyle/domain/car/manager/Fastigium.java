package com.itstyle.domain.car.manager;

import lombok.Data;

@Data
public class Fastigium {
    private String startTime;

    private String endTime;

    private boolean status = false;

    public static Fastigium buildDefault() {
        Fastigium fastigium = new Fastigium();
        fastigium.setStartTime("");
        fastigium.setEndTime("");
        fastigium.setStatus(false);
        return fastigium;
    }

}
