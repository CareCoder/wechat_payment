package com.itstyle.domain.caryard;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "equipment_status")
@DynamicUpdate
public class EquipmentStatus {

    @Id
    @GeneratedValue
    private Long id;
    private String equipmentName;
    private Integer entranceOneStatus;
    private Integer entranceTwoStatus;
    private Integer exportOneStatus;
    private Integer exportTwoStatus;
}
