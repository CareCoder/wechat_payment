package com.itstyle.vo.login.reponse;

import lombok.Data;

@Data
public class LoginResponse {
    private String serviceCode;
    private String errorCode;
    private String errorDesc;
}
