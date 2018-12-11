package com.itstyle.vo.inition.response;

import lombok.Data;

@Data
public class AccessAuthoritySetup {

    public String name;//通道的名称

    public String type;//通道的type

    public String ip;//通道的ip

    public String cameraIp; // 辅助相机IP

    public boolean blueCar;//	true表示 入口1允许临时车入场，false则不允许

    public boolean yellowCar;//true表示 入口1允许月租车入场，false则不允许

    public boolean greenCar;//true表示 入口1允许特种车入场，false则不允许

    public boolean blackCar;//	true表示 入口2允许临时车入场，false则不允许

    public boolean monlyCar_A;//true表示 入口2允许月租车入场，false则不允许

    public boolean monlyCar_B;//true表示 入口2允许特种车入场，false则不允许

    public boolean monlyCar_C;

    public boolean specialCar;
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
