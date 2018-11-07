package com.itstyle.common;

import com.itstyle.domain.log.SysLogger;

import java.util.Date;

public class SystemLoggerHelper {

    public static void log(String username, String action, String desc) {
        SysLogger log = new SysLogger(username, action, desc, new Date());
        SystemLogQueue.SYSTEM_LOG_QUEUE.add(log);
    }
}
