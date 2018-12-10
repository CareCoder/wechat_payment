package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarColor;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * 月租车
 */
@Data
@Entity
@Table(name = "month_car_info", uniqueConstraints = {@UniqueConstraint(columnNames="carNum")})
@DynamicUpdate
public class MonthCarInfo extends BaseEntity {
    private String name;

    private String carNum;

    private String phone;

    private CarType carType;

    private Long startTime;

    private Long endTime;

    private String carGroup;

    private String remarks;

    /**
     * 身份证号码
     */
    private String idCardNum;

    private CarColor carColor;
}
