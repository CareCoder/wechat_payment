package com.itstyle.domain.feesettings.response;

import lombok.Data;

@Data
public class ByChargesResponse {
    private Integer chargeModel;
    private ChargeRule chargeRule;

    @Data
    public static class ChargeRule {
        private String carType;
        /** 最高收费，以分为单位(24小时为一个周期) */
        private Integer cappingAmount;
        /** 可选周期 */
        private Integer optionalCycle;
        /** 免费时间（分钟） */
        private Integer freeTime;
        /** 一次性收费 */
        private Long oneTimeCharge;
        /** 中央收费时间 */
//        private Integer centralChargesTime;
    }
}
