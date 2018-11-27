package com.itstyle.vo.inition.response;

import lombok.Data;

@Data
public class AccessAuthoritySetup {

    public String name;//通道的名称

    public String type;//通道的type

    public String ip;//通道的ip

    public String cameraIp; // 辅助相机IP

    public boolean tempCar_1;//	true表示 入口1允许临时车入场，false则不允许

    public boolean monlyCar_1;//true表示 入口1允许月租车入场，false则不允许

    public boolean specialCar_1;//true表示 入口1允许特种车入场，false则不允许

    public boolean tempCar_2;//	true表示 入口2允许临时车入场，false则不允许

    public boolean monlyCar_2;//true表示 入口2允许月租车入场，false则不允许

    public boolean specialCar_2;//true表示 入口2允许特种车入场，false则不允许
//
//    public boolean exit_tempCar_1;//true表示 出口1允许临时车出场，false则不允许
//
//    public boolean exit_monlyCar_1;//	true表示 出口1允许月租车出场，false则不允许
//
//    public boolean exit_specialCar_1;//	true表示 出口1允许特种车出场，false则不允许
//
//    public boolean exit_tempCar_2;//	true表示 出口2允许临时车出场，false则不允许
//
//    public boolean exit_monlyCar_2;//true表示 出口2允许月租车出场，false则不允许
//
//    public boolean exit_specialCar_2;//true表示 出口2允许特种车出场，false则不允许
}
