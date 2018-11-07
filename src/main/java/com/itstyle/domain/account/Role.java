package com.itstyle.domain.account;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
@DynamicUpdate
public class Role extends BaseEntity {
    private Long id;
    private String name;
}
