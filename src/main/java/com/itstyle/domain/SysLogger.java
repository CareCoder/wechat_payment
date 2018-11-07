package com.itstyle.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogger {
    private String username;
    private String action;
    private String desc;
    private Date logDate;
}
