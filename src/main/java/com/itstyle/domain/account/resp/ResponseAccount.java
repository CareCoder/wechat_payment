package com.itstyle.domain.account.resp;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseAccount {
    private Long id;
    private String username;
    private String account;
    private String password;
    private Long roleId;
    private String remark;
    private Date createTime;
    private Date modifyTime;
    private String roleName;
}
