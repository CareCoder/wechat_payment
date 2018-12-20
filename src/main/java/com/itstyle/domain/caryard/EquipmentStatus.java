package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class EquipmentStatus {

    private String passWayName;//通道名

    private Boolean networkType;//网络类型，分为 internet（正常）和mobile(不正常)

    private Boolean camera;//true（正常）/false

    private Boolean display;//true（正常）/false

    private Boolean roadGate;//开闸状态open 关闸状态close

}
