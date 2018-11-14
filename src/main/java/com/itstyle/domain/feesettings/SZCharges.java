package com.itstyle.domain.feesettings;

import lombok.Data;

/**
 * 深圳收费
 */
@Data
public class SZCharges {
    private String carType;
    /** 最高收费，以分为单位(24小时为一个周期) */
    private Long maximumCharges;
    /** 免费时间（分钟） */
    private Integer freeTime;

    /**
     * 工作日
     */
    @Data
    public static class WorkingDay {

        /**
         * 高峰期
         */
        @Data
        public static class Peak {
            /** 高峰期开始时间 */
            private Long statTime;
            /** 高峰期结束时间 */
            private Long endTime;
            private FeeSettings feeSettings;
        }

        /**
         * 非高峰期
         */
        @Data
        public static class OffPeak {
            private FeeSettings feeSettings;
        }
    }

    /**
     * 非工作日
     */
    @Data
    public static class NonWorkingDay {
        private FeeSettings feeSettings;
    }

    /**
     * 收费设置
     */
    @Data
    public static class FeeSettings {
        /** 首段时长 */
        private Integer firstDuration;
        /** 首段金额（分） */
        private Long firstAmount;
        /** 递增时长（分钟） */
        private Long increasingTime;
        /** 递增金额 */
        private Long increasingAmount;
    }
}
