package com.itstyle.domain.car.manager.enums;

public enum ChargeType {
    CASH_PAYMENT(0, "线下支付"),
    ONLINE_PAYMENT(1, "线上支付");

    private int value;
    private String name;

    ChargeType(int value, String name) {
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
