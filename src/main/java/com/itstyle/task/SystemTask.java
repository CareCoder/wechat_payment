package com.itstyle.task;

import com.itstyle.handler.MyTextWebSocketHandler;
import com.itstyle.service.ExternalInterfaceService;
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
        Integer aLong = logService.doSystemLogSave();
        if (aLong > 0) {
            log.info("[SystemTask] 定时任务：成功入库 " + aLong + " 条系统日志");
        }
    }

    @Scheduled(fixedRate = 45000L)
    public void pingPong() {
        long now = System.currentTimeMillis();
        MyTextWebSocketHandler.pingPoneMap.entrySet().stream()
                .filter(e -> now - e.getValue() > 30000L)
                .forEach(e -> {
                    String username = e.getKey();
                    log.info("pingPong timeout, username= {}", username);
                    MyTextWebSocketHandler.closeSession(username);
                });
    }
}
