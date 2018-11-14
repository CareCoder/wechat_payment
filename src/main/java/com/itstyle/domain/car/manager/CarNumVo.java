package com.itstyle.domain.car.manager;

import com.itstyle.domain.car.manager.enums.CarNumType;
import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "car_num", indexes = {@Index(name = "path_index", columnList = "path", unique = true)})
@DynamicUpdate
public class CarNumVo extends BaseEntity {
    private String carNum;
    private CarNumType carNumType;
    private Long time;
    private String path;
    private String uuid;
}
