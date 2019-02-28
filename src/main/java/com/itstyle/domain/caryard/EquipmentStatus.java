package com.itstyle.domain.caryard;

import lombok.Data;

@Data
public class EquipmentStatus {

    private String passWayName;//通道名

    private Boolean networkType;//网络类型，分为 internet（正常）和mobile(不正常)

    private Boolean camera;//true（正常）/false                     摄像机

    private String gadget;//0(正常)/1(故障)         /3(无)         扫码器

    private String printer;//0(正常)/1(故障)/2(纸尽)/3(无)         打印机

    private String cashCode;// 0(正常)/1(故障)/2(离线)/3(无)       钱箱

}
