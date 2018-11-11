package com.itstyle.domain.car.manager;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 月租车
 */
@Data
@Entity
@Table(name = "month_car_info")
@DynamicUpdate
public class MonthCarInfo extends BaseEntity {
    private String carNum;

    private String carOwnerName;

    private String phone;

    private String carType;

    private String startTime;

    private String endTime;

    private String carGroup;

    private String remarks;
}
