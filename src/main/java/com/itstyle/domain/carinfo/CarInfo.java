package com.itstyle.domain.carinfo;

import lombok.Data;

@Data
public class CarInfo {
    private Long id;

    private String name;

    private String carNum;

    private String phone;

    private String remarks;

    private Long createTime;

    private Long modifyTime;

    private Boolean isFree;

    private Boolean isBlackList;
}
