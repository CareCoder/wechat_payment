package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class EquipmentStatus {

    private String key;
    private String equipmentName;
    private Integer entranceOneStatus;
    private Integer entranceTwoStatus;
    private Integer exportOneStatus;
    private Integer exportTwoStatus;
}
