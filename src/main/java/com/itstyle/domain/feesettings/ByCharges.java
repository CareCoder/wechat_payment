package com.itstyle.domain.feesettings;

import lombok.Data;

/**
 * 按次收费
 */
@Data
public class ByCharges {
    private String carType;
    /** 最高收费，以分为单位(24小时为一个周期) */
    private Integer cappingAmount;
    /** 免费时间（分钟） */
    private Integer freeTime;
    /** 一次性收费 */
    private Long oneTimeCharge;
    /** 中央收费时间 */
    private Integer centralChargesTime;
}
