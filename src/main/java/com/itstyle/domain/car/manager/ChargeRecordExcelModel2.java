package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.domain.report.ChargeRecord;
import com.itstyle.utils.BusinessUtils;
import com.itstyle.utils.DateUtil;
import lombok.Data;

import java.util.List;

@Data
public class ChargeRecordExcelModel2 extends BaseRowModel{
    @ExcelProperty(value = "车牌号" ,index = 0)
    private String carNum;
    @ExcelProperty(value = "起始时间" ,index = 1)
    private String enterTime;
    @ExcelProperty(value = "有效期至" ,index = 2)
    private String leaveTime;
    @ExcelProperty(value = "车辆类型" ,index = 3)
    private String carRealType;
    @ExcelProperty(value = "收费类型" ,index = 4)
    private String chargeType;
    @ExcelProperty(value = "实收金额" ,index = 5)
    private Integer fee;
    @ExcelProperty(value = "续费时间" ,index = 6)
    private String time;
    @ExcelProperty(value = "收费人员" ,index = 7)
    private String chargePersonnel;

    public static ChargeRecordExcelModel2 convert(ChargeRecord c, List<FixedCarManager> f) {
        ChargeRecordExcelModel2 m = new ChargeRecordExcelModel2();
        m.setCarNum(c.getCarNum());
        m.setEnterTime(DateUtil.format(c.getEnterTime()));
        m.setLeaveTime(DateUtil.format(c.getLeaveTime()));
        m.setCarRealType(BusinessUtils.fetchCustomName(c.getCarRealType(), f));
        m.setChargeType(c.getChargeType() == null ? "" : c.getChargeType().getName());
        m.setFee(c.getFee());
        m.setTime(DateUtil.format(c.getTime()));
        m.setChargePersonnel(c.getChargePersonnel());
        return m;
    }
}
