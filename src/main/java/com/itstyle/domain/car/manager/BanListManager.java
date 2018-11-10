package com.itstyle.domain.car.manager;

import lombok.Data;

@Data
public class BanListManager {
    private Long startTime;

    private Long endTime;

    private boolean tempCarA = false;

    private boolean tempCarB = false;

    private boolean tempCarC = false;

    private boolean monthCarA = false;

    private boolean monthCarB = false;

    private boolean monthCarC = false;

    private boolean vipCar = false;
}
