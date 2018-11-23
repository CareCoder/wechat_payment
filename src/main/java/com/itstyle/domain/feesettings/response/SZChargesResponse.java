package com.itstyle.domain.feesettings.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SZChargesResponse {

    private Integer chargeModel;
    private ChargeRule chargeRule;

    @Data
    public static class ChargeRule {
        private String carType;
        /** 最高收费，以分为单位(24小时为一个周期) */
        private Long cappingAmount;
        /** 可选周期 */
        private Integer optionalCycle;
        /** 免费时间（分钟） */
        private Integer freeTime;
        /** 中央收费时间 */
        private Integer centralChargesTime;
        /** 工作日高峰期开始时间 */
        @SerializedName("peakModel_peakTimeStart")
        private Integer peakModelPeakTimeStart;
        /** 工作日高峰期结束时间 */
        @SerializedName("peakModel_peakTimeEnd")
        private Integer peakModelPeakTimeEnd;

        /** 高峰期首段停车时间 */
        @SerializedName("peak_firstTime")
        private Integer peakFirstTime;

        /** 高峰期首段时间的金额 */
        @SerializedName("peak_firstAmount")
        private Double peakFirstAmount;

        /** 高峰期内首段过后的递增时间 */
        @SerializedName("peak_increasingTime")
        private Integer peakIncreasingTime;

        /** 高峰期内首段过后的递增金额 */
        @SerializedName("peak_increasingAmount")
        private Double peakIncreasingAmount;

        /** 非高峰期首段停车时间 */
        @SerializedName("nonPeak_firstTime")
        private Integer nonPeakFirstTime;

        /** 非高峰期首段时间的金额 */
        @SerializedName("nonPeak_firstAmount")
        private Double nonPeakFirstAmount;

        /** 非高峰期内首段过后的递增时间 */
        @SerializedName("nonPeak_increasingTime")
        private Integer nonPeakIncreasingTime;

        /** 非高峰期内首段过后的递增金额 */
        @SerializedName("nonPeak_increasingAmount")
        private Double nonPeakIncreasingAmount;

        /** 非工作日首段停车时间 */
        @SerializedName("nonWorkingDay_firstTime")
        private Integer nonWorkingDayFirstTime;

        /** 非工作日首段时间的金额 */
        @SerializedName("nonWorkingDay_firstAmount")
        private Double nonWorkingDayFirstAmount;

        /** 非工作日首段过后的递增时间 */
        @SerializedName("nonWorkingDay_increasingTime")
        private Integer nonWorkingDayIncreasingTime;

        /** 非工作日首段过后的递增金额 */
        @SerializedName("nonWorkingDay_increasingAmount")
        private Double nonWorkingDayIncreasingAmount;
    }
}
