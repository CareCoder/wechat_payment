package com.itstyle.domain.carinfo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CarInfo {
    private Long id;

    private String name;

    private String carNum;

    private String phone;

    private String remarks;

    private Timestamp createTime;

    private Timestamp modifyTime;

    private Boolean isFree;

    private Boolean isBlackList;
}
