package com.itstyle.domain.car.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.itstyle.domain.log.SysLoggerResponse;
import com.itstyle.utils.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
public class LogExcelModel extends BaseRowModel{
    @ExcelProperty(value = "用户名" ,index = 0)
    private String username;
    @ExcelProperty(value = "操作员" ,index = 1)
    private String roleName;
    @ExcelProperty(value = "操作" ,index = 2)
    private String action;
    @ExcelProperty(value = "描述" ,index = 3)
    private String desc;
    @ExcelProperty(value = "操作时间" ,index = 4)
    private String logDate;

    public static LogExcelModel convert(SysLoggerResponse c) {
        LogExcelModel m = new LogExcelModel();
        m.setUsername(c.getUsername());
        m.setAction(c.getAction());
        m.setDesc(c.getTDescribe());
        m.setLogDate(DateUtil.format(c.getLogDate().getTime()));
        m.setRoleName(c.getRoleName());
        return m;
    }
}
