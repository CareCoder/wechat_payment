package com.itstyle.domain.account;

import lombok.Data;

import java.util.Date;

@Data
public class Account {
    private Long id;
    private String username;
    private String account;
    private String password;
    private Long roleId;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private String roleName;
}
