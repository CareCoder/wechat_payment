package com.itstyle.domain.report;

import lombok.Data;

@Data
public class ChargeRecordStatistics {
    /**
     * 收费金额
     */
    private Integer totleFee;
    /**
     * 应收金额
     */
    private Integer totleReceivableFee;

    /**
     * 折扣金额
     */
    private Integer totleDiscountAmount;
}
