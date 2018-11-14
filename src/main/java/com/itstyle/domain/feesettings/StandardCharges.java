package com.itstyle.domain.feesettings;

import lombok.Data;

/**
 * 标准收费
 */
@Data
public class StandardCharges {
    private String carType;
    /** 最高收费，以分为单位(24小时为一个周期) */
    private Long maximumCharges;
    /** 免费时间（分钟） */
    private Integer freeTime;
    /** 首段时长 */
    private Integer firstDuration;
    /** 首段金额（分） */
    private Long firstAmount;
    /** 递增时长（分钟） */
    private Long increasingTime;
    /** 递增金额 */
    private Long increasingAmount;

}
