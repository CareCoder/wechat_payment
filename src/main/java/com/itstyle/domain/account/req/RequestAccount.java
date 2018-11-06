package com.itstyle.domain.account.req;

import lombok.Data;

import java.util.Date;

@Data
public class RequestAccount {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private Long roleId;
    private String remark;
    private Date updateTime;
}
