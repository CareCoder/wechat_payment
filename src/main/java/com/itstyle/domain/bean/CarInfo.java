package com.itstyle.domain.bean;

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
}
