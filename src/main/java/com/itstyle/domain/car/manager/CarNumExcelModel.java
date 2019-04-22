package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.utils.DateUtil;
import lombok.Data;

@Data
public class CarNumExcelModel extends BaseRowModel{
    @ExcelProperty(value = "车牌号" ,index = 0)
    private String carNum;
    @ExcelProperty(value = "车辆类型" ,index = 1)
    private String carType;
    @ExcelProperty(value = "入场时间" ,index = 2)
    private String time;
    @ExcelProperty(value = "出场时间" ,index = 3)
    private String leaveTime;
    @ExcelProperty(value = "入口通道" ,index = 4)
    private String enterPass;
    @ExcelProperty(value = "出口通道" ,index = 5)
    private String leavePass;
    @ExcelProperty(value = "入场类型" ,index = 6)
    private String enterWay;

    public static CarNumExcelModel convert(CarNumVo c) {
        CarNumExcelModel m = new CarNumExcelModel();
        m.setCarNum(c.getCarNum());
        m.setCarType(c.getCarType() == null ? "" : c.getCarType().getName());
        m.setTime(DateUtil.format(c.getTime()));
        m.setLeaveTime(DateUtil.format(c.getLTime()));
        m.setEnterPass(c.getEnterPass());
        m.setLeavePass(c.getLeavePass());
        m.setEnterWay(c.getEnterWay());
        return m;
    }
}
