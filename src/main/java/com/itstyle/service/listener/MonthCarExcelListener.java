package com.itstyle.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.itstyle.domain.car.manager.CarInfoExcelModel;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.MonthCarInfoService;

public class MonthCarExcelListener extends AnalysisEventListener {
    private MonthCarInfoService monthCarInfoService;

    public MonthCarExcelListener(MonthCarInfoService monthCarInfoService) {
        this.monthCarInfoService = monthCarInfoService;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        doSomething(o);
    }

    private void doSomething(Object o) {
        MonthCarInfo monthCarInfo = CarInfoExcelModel.convert((CarInfoExcelModel) o);
        monthCarInfoService.edit(monthCarInfo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
