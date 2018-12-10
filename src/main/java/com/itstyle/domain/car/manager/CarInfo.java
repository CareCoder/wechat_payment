package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarColor;
import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "car_info", uniqueConstraints = {@UniqueConstraint(columnNames="carNum")})
@DynamicUpdate
public class CarInfo extends BaseEntity {
    private String name;

    private String carNum;

    private String phone;

    private String remarks;

    private Boolean isFree;

    private Boolean isBlackList;

    private CarColor carColor;
}
