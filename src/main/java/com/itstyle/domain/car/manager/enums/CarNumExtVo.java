package com.itstyle.domain.car.manager.enums;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "car_num_ext" , indexes = {@Index(name = "path_index", columnList = "path", unique = true)})
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
