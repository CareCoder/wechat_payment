package com.itstyle.domain.fastigium;

import com.itstyle.utils.hibernate.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "car_info")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Fastigium extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Date startTime;

    private Date endTime;

    private boolean status;
}
