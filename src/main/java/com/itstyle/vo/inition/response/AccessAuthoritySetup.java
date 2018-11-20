package com.itstyle.vo.inition.response;

public class AccessAuthoritySetup {
    public Integer id;//通道的id

    public Integer name;//通道的名称

    public Integer type;//通道的type

    public String ip;//通道的ip

    public boolean entrance_tempCar_1;//	true表示 入口1允许临时车入场，false则不允许

    public boolean entrance_monlyCar_1;//true表示 入口1允许月租车入场，false则不允许

    public boolean entrance_specialCar_1;//true表示 入口1允许特种车入场，false则不允许

    public boolean entrance_tempCar_2;//	true表示 入口2允许临时车入场，false则不允许

    public boolean entrance_monlyCar_2;//true表示 入口2允许月租车入场，false则不允许

    public boolean entrance_specialCar_2;//true表示 入口2允许特种车入场，false则不允许

    public boolean exit_tempCar_1;//true表示 出口1允许临时车出场，false则不允许

    public boolean exit_monlyCar_1;//	true表示 出口1允许月租车出场，false则不允许

    public boolean exit_specialCar_1;//	true表示 出口1允许特种车出场，false则不允许

    public boolean exit_tempCar_2;//	true表示 出口2允许临时车出场，false则不允许

    public boolean exit_monlyCar_2;//true表示 出口2允许月租车出场，false则不允许

    public boolean exit_specialCar_2;//true表示 出口2允许特种车出场，false则不允许
}
