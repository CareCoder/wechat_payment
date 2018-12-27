package com.itstyle.domain.report;

import lombok.Data;

@Data
public class ChargeRecordStatistics {
    /**
     * 收费金额
     */
    private String totleFee;
    /**
     * 应收金额
     */
    private String totleReceivableFee;

    /**
     * 折扣金额
     */
    private String totleDiscountAmount;
}
