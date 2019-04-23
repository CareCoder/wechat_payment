package com.itstyle.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.car.manager.CarInfoExcelModel;
import com.itstyle.domain.car.manager.FixedCarManager;
import com.itstyle.domain.car.manager.MonthCarInfo;
import com.itstyle.service.GlobalSettingService;
import com.itstyle.service.MonthCarInfoService;
import com.itstyle.utils.DateUtil;

import java.util.List;

public class MonthCarExcelListener extends AnalysisEventListener {
    private GlobalSettingService globalSettingService;
    private MonthCarInfoService monthCarInfoService;
    private Account account;

    public MonthCarExcelListener(GlobalSettingService globalSettingService,MonthCarInfoService monthCarInfoService, Account account) {
        this.globalSettingService = globalSettingService;
        this.monthCarInfoService = monthCarInfoService;
        this.account = account;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        doSomething(o);
    }

    private void doSomething(Object o) {
        List<FixedCarManager> f = globalSettingService.list(YstCommon.FIXEDCARMANAGER_KEY, FixedCarManager.class);
        MonthCarInfo monthCarInfo = CarInfoExcelModel.convert((CarInfoExcelModel) o, f);
        Integer month = DateUtil.calcMonth(monthCarInfo.getStartTime(), monthCarInfo.getEndTime());
        monthCarInfoService.edit(monthCarInfo,month,account);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
