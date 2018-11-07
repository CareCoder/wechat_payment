package com.itstyle.domain.carinfo;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "car_info")
@DynamicUpdate
public class CarInfo extends BaseEntity {
    private String name;

    private String carNum;

    private String phone;

    private String remarks;

    private Boolean isFree;

    private Boolean isBlackList;
}
