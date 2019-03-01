package com.itstyle.domain.car.manager;

import lombok.Data;

@Data
public class Fastigium {
    private String exit;//出口
    private String exitStartTime;//出口开始时间

    private String exitEndTime;//出口结束时间

    private String entry;//入口
    private String entryStartTime;//入口开始时间

    private String entryEndTime;//入口结束时间

    private boolean exitStatus = false;

    private boolean entryStatus = false;

    public static Fastigium buildDefault() {
        Fastigium fastigium = new Fastigium();
        fastigium.setEntryEndTime("");
        fastigium.setEntryStartTime("");
        fastigium.setEntryStatus(false);
        fastigium.setExitEndTime("");
        fastigium.setExitStartTime("");
        fastigium.setExitStatus(false);
        return fastigium;
    }

}
