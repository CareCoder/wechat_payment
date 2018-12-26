package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.domain.car.manager.enums.CarColor;
import com.itstyle.domain.car.manager.enums.CarType;
import com.itstyle.utils.CommonUtils;
import com.itstyle.utils.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class CarInfoExcelModel extends BaseRowModel {
    @ExcelProperty(value = "姓名" ,index = 0)
    private String name;

    @ExcelProperty(value = "车牌" ,index = 1)
    private String carNum;

    @ExcelProperty(value = "手机号" ,index = 2)
    private String phone;

    @ExcelProperty(value = "车辆类型" ,index = 3)
    private String carType;

    @ExcelProperty(value = "开始时间" ,index = 4)
    private String startTime;

    @ExcelProperty(value = "到期时间" ,index = 5)
    private String endTime;

    @ExcelProperty(value = "车位组" ,index = 6)
    private String carGroup;

    @ExcelProperty(value = "备注" ,index = 7)
    private String remarks;

    @ExcelProperty(value = "身份证号码" ,index = 8)
    private String idCardNum;

    @ExcelProperty(value = "车颜色" ,index = 9)
    private String carColor;

    public static CarInfoExcelModel convert(MonthCarInfo m) {
        CarInfoExcelModel c = new CarInfoExcelModel();
        c.setName(m.getName());
        c.setCarNum(m.getCarNum());
        c.setPhone(m.getPhone());
        c.setCarType(m.getCarType() != null ? m.getCarType().getName() : "");
        c.setStartTime(DateUtil.format(m.getStartTime()));
        c.setEndTime(DateUtil.format(m.getEndTime()));
        c.setCarGroup(m.getCarGroup());
        c.setRemarks(m.getRemarks());
        c.setIdCardNum(m.getIdCardNum());
        c.setCarColor(m.getCarColor() != null ? m.getCarColor().getName() : "");
        return c;
    }

    public static MonthCarInfo convert(CarInfoExcelModel c) {
        MonthCarInfo m = new MonthCarInfo();
        m.setName(c.getName());
        m.setCarNum(c.getCarNum());
        m.setPhone(c.getPhone());
        if (c.getCarType() != null) {
            m.setCarType(CommonUtils.fetchEnum(c.carType, CarType.class));
        }
        m.setStartTime(Objects.requireNonNull(DateUtil.parse(c.getStartTime())).getTime());
        m.setEndTime(Objects.requireNonNull(DateUtil.parse(c.getEndTime())).getTime());
        m.setCarGroup(c.getCarGroup());
        m.setRemarks(c.getRemarks());
        m.setIdCardNum(c.getIdCardNum());
        if (c.getCarColor() != null) {
            m.setCarColor(CommonUtils.fetchEnum(c.getCarColor(), CarColor.class));
        }
        return m;
    }
}
