package com.itstyle.domain.car.manager.enums;

public enum CarColor {
    LC_BLUE(0, "蓝色"),
    LC_YELLOW(1, "黃色"),
    LC_BLACK(2, "黑色"),
    LC_GREEN(3, "绿色");

    private int value;
    private String name;

    CarColor(int value, String name) {
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
