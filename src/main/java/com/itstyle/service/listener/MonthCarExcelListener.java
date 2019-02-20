package com.itstyle.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarInfoExcelModel;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.MonthCarInfoService;
import com.itstyle.utils.DateUtil;

public class MonthCarExcelListener extends AnalysisEventListener {
    private MonthCarInfoService monthCarInfoService;
    private Account account;

    public MonthCarExcelListener(MonthCarInfoService monthCarInfoService, Account account) {
        this.monthCarInfoService = monthCarInfoService;
        this.account = account;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        doSomething(o);
    }

    private void doSomething(Object o) {
        MonthCarInfo monthCarInfo = CarInfoExcelModel.convert((CarInfoExcelModel) o);
        Integer month = DateUtil.calcMonth(monthCarInfo.getStartTime(), monthCarInfo.getEndTime());
        monthCarInfoService.edit(monthCarInfo,month,account);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
