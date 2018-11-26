package com.itstyle.domain.account;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "account")
@DynamicUpdate
public class Account extends BaseEntity {
    private String username;
    private String tAccount;
    private String password;
    private Long roleId;
    private String remark;
    private String openid;
    private String unionid;
}
