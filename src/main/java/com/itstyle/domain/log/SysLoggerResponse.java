package com.itstyle.domain.log;

import lombok.Data;

import java.util.Date;

@Data
public class SysLoggerResponse {
    private Long id;
    private String username;
    private String action;
    private String tDescribe;
    private Long roleId;
    private Date logDate;
    private String roleName;
}
