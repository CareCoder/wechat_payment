package com.itstyle.domain.vo;

import lombok.Data;

@Data
public class LoginResponseVo {
    private String serviceCode;
    private int errorCode;
    private String errorDesc;
}
