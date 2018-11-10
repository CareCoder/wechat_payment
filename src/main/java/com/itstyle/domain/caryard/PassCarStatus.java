package com.itstyle.domain.caryard;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pass_car_status")
@DynamicUpdate
public class PassCarStatus {

    public static final Integer BAN = 0;
    public static final Integer ALLOW = 1;

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer status;
    private String channelDesc;
}
