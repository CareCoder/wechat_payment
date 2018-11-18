package com.itstyle.domain.vo;

import lombok.Data;

@Data
public class LoginRequestVo {
    private String serviceCode;
    private String dataSrc;
    private String userName;
    private String password;
    private String login;
}