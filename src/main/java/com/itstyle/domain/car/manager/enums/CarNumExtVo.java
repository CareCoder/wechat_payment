package com.itstyle.domain.car.manager.enums;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "car_num_ext")
@Entity
public class CarNumExtVo {
    @Id
    @GeneratedValue
    private Long id;

    private CarNumType carNumType;

    private String path;

    private String uuid;

    private Long time;

}
