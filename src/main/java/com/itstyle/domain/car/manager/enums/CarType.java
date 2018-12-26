package com.itstyle.domain.car.manager.enums;

public enum CarType {
    TEMP_CAR_A(0, "蓝牌"),
    TEMP_CAR_B(1, "黄牌"),
    TEMP_CAR_C(2, "黑牌"),
    TEMP_CAR_D(3, "绿牌"),
    MONTH_CAR_A(4, "月租车A"),
    MONTH_CAR_B(5, "月租车B"),
    MONTH_CAR_C(6, "月租车C"),
    VIP_CAR(7, "免费车");

    private int value;
    private String name;

    CarType(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
