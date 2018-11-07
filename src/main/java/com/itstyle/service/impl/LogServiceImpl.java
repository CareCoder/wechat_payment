package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLogQueue;
import com.itstyle.domain.SysLogger;
import com.itstyle.mapper.LogMapper;
import com.itstyle.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private LogMapper logMapper;

    @Autowired
    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }


    @Override
    public Long doSystemLogSave() {
        List<SysLogger> list = new ArrayList<>();
        Queue<SysLogger> queue = SystemLogQueue.SYSTEM_LOG_QUEUE;
        while (!queue.isEmpty()) {
            SysLogger log = queue.poll();
            if (log == null) {
                continue;
            }
            list.add(log);
        }
        if (list.size() == 0) {
            return 0L;
        }
        return logMapper.insertBatch(list);
    }

    @Override
    public PageResponse<SysLogger> getAll(int page, int size) {
        return null;
    }
}
