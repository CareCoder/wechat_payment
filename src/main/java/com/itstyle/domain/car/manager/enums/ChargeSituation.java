package com.itstyle.domain.car.manager.enums;

public enum ChargeSituation {
    FREE_TIME(0, "免费时长"),
    NORMAL_CHARGE(1, "正常收费"),
    OVERTIME_CHARGE(2, "超时收费"),
    CEILING(3, "最高限额");

    private int value;
    private String name;

    ChargeSituation(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
