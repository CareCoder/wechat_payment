package com.itstyle.domain.fastigium;

import lombok.Data;

import java.util.Date;

@Data
public class Fastigium {
    private Date startTime;

    private Date endTime;

    private boolean status;

    public static Fastigium buildDefault() {
        Fastigium fastigium = new Fastigium();
        fastigium.setStartTime(new Date());
        fastigium.setEndTime(new Date());
        fastigium.setStatus(false);
        return fastigium;
    }

}
