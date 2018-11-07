package com.itstyle.domain.fastigium;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "fastigium")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Fastigium{
    @Id
    @GeneratedValue
    private Long id;

    private Date startTime;

    private Date endTime;

    private boolean status;
}
