package com.itstyle.common;

import com.itstyle.domain.log.SysLogger;

import java.util.Date;

public class SystemLoggerHelper {

    public static void log(String username, Long roleId, String action, String tDescribe) {
        SysLogger log = new SysLogger(username, roleId, action, tDescribe, new Date());
        SystemLogQueue.SYSTEM_LOG_QUEUE.add(log);
    }
}
