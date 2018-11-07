package com.itstyle.task;

import com.itstyle.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemTask {

    private LogService logService;

    @Autowired
    public SystemTask(LogService logService) {
        this.logService = logService;
    }

    @Scheduled(fixedRate = 5000L)
    public void doSystemLogSave() {
        log.info("[SystemTask] 定时日志保存任务");
        Integer aLong = logService.doSystemLogSave();
        if (aLong > 0) {
            log.info("[SystemTask] 定时任务：成功入库 " + aLong + " 条系统日志");
        }
    }
}
