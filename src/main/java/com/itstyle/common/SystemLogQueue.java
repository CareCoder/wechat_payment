package com.itstyle.common;

import com.itstyle.domain.log.SysLogger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SystemLogQueue {
    public static Queue<SysLogger> SYSTEM_LOG_QUEUE = new ConcurrentLinkedDeque<>();
}
