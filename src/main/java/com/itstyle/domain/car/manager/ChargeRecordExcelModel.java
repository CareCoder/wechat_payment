package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.domain.car.manager.enums.ChargeSituation;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.utils.BusinessUtils;
import com.itstyle.utils.DateUtil;
import lombok.Data;

import java.util.List;

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
    @ExcelProperty(value = "入场时间" ,index = 1)
    private String enterTime;

    /**
     * 出场时间
     */
    @ExcelProperty(value = "出场时间" ,index = 2)
    private String leaveTime;
    /**
     * 车辆真实类型
     */
    @ExcelProperty(value = "车辆类型" ,index = 3)
    private String carRealType;

    /**
     * 应收金额
     */
    @ExcelProperty(value = "车牌号码" ,index = 4)
    private Integer receivableFee;

    /**
     * 优惠金额
     */
    @ExcelProperty(value = "优惠金额" ,index = 5)
    private Integer discountAmount;

    /**
     * 实收金额
     */
    @ExcelProperty(value = "实收金额" ,index = 6)
    private Integer fee;

    /**
     * 收费类型
     */
    @ExcelProperty(value = "收费类型" ,index = 7)
    private String chargeType;


    /**
     * 收费情况
     */
    @ExcelProperty(value = "收费情况" ,index = 8)
    private String chargeSituation;

    /**
     * 收费员
     */
    @ExcelProperty(value = "收费员" ,index = 9)
    private String chargePersonnel;

    public static ChargeRecordExcelModel convert(ChargeRecord c,  List<FixedCarManager> f) {
        ChargeRecordExcelModel m = new ChargeRecordExcelModel();
        m.setCarNum(c.getCarNum());
        m.setEnterTime(DateUtil.format(c.getEnterTime()));
        m.setLeaveTime(DateUtil.format(c.getLeaveTime()));
        m.setCarRealType(BusinessUtils.fetchCustomName(c.getCarRealType(), f));
        m.setReceivableFee(c.getReceivableFee());
        m.setDiscountAmount(c.getDiscountAmount());
        m.setFee(c.getFee());
        m.setChargeType(c.getChargeType() == null ? "" : c.getChargeType().getName());
        m.setChargeSituation(c.getChargeSituation() == null ? "" : c.getChargeSituation().getName());
        m.setChargePersonnel(c.getChargePersonnel());
        return m;
    }
}
