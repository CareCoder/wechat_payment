package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.utils.DateUtil;
import lombok.Data;

@Data
public class ChargeRecordExcelModel  extends BaseRowModel{
    /**
     * 车牌号码
     */
    @ExcelProperty(value = "车牌号码" ,index = 0)
    private String carNum;

    /**
     * 入场时间
     */
    @ExcelProperty(value = "开始时间" ,index = 1)
    private String enterTime;

    /**
     * 出场时间
     */
    @ExcelProperty(value = "结束时间" ,index = 2)
    private String leaveTime;
    /**
     * 车辆真实类型
     */
    @ExcelProperty(value = "车辆类型" ,index = 3)
    private String carRealType;

    /**
     * 收费金额
     */
    @ExcelProperty(value = "收费金额" ,index = 4)
    private Integer fee;

    /**
     * 折扣金额
     */
    @ExcelProperty(value = "折扣金额" ,index = 5)
    private Integer discountAmount;

    /**
     * 收费类型
     */
    @ExcelProperty(value = "收费类型" ,index = 6)
    private String chargeType;


    /**
     * 收费员
     */
    @ExcelProperty(value = "收费员" ,index = 7)
    private String chargePersonnel;

    public static ChargeRecordExcelModel convert(ChargeRecord c) {
        ChargeRecordExcelModel m = new ChargeRecordExcelModel();
        m.setCarNum(c.getCarNum());
        m.setEnterTime(DateUtil.format(c.getEnterTime()));
        m.setLeaveTime(DateUtil.format(c.getLeaveTime()));
        m.setCarRealType(c.getCarRealType() == null ? "" : c.getCarRealType().toString());
        m.setFee(c.getFee());
        m.setDiscountAmount(c.getDiscountAmount());
        m.setChargeType(c.getChargeType() == null ? "" : c.getChargeType().toString());
        m.setChargePersonnel(c.getChargePersonnel());
        return m;
    }
}
