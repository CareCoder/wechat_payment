package com.itstyle.vo.login.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String serviceCode;
    private String dataSrc;
    private String userName;
    private String password;
    private String login;
}