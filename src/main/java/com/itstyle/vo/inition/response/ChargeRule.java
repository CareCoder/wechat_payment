package com.itstyle.vo.inition.response;

public class ChargeRule {
    public int plateColor;//	车牌颜色的索引值

    public int cappingAmount;//	一天内的封顶金额

    public int freeTime;//	免费停车时间，单位：分钟

    public int firstTime;//	首段停车时间，单位：分钟

    public int firstAmount;//	首段停车时间的金额

    public int increasingTime;//	首段过后的递增时间, 单位：分钟

    public int increasingAmount;//	首段停车时间过后的递增金额

    public int optionalCycle;//	可选一天or24小时，周期后就要启用首段时间 0: 一天，1：24小时
}
