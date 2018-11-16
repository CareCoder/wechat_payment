package com.itstyle.domain.feesettings;

import lombok.Data;

/**
 * 标准收费
 */
@Data
public class StandardCharges {
    private String carType;
    /** 免费时间（分钟） */
    private Integer freeTime;
    /** 最高收费，以分为单位(24小时为一个周期) */
    private Integer cappingAmount;
    /** 中央收费时间 */
    private Integer centralChargesTime;
    /** 首段时长 */
    private Integer firstTime;
    /** 首段金额（元） */
    private Double firstAmount;
    /** 递增时长（分钟） */
    private Long increasingTime;
    /** 递增金额 */
    private Double increasingAmount;

}
