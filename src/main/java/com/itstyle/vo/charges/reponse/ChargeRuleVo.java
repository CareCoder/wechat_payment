package com.itstyle.vo.charges.reponse;

import lombok.Data;

@Data
public class ChargeRuleVo {
    private int plateColor;//	车牌颜色的索引值

    private int cappingAmount;//	一天内的封顶金额

    private int freeTime;//	免费停车时间，单位：分钟

    private int firstTime;//	首段停车时间，单位：分钟

    private double firstAmount;//	首段停车时间的金额

    private int increasingTime;//	首段过后的递增时间, 单位：分钟

    private double increasingAmount;//	首段停车时间过后的递增金额

    private int optionalCycle;//	可选一天or24小时，周期后就要启用首段时间 0: 一天，1：24小时
}
