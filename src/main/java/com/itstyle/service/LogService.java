package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.log.SysLoggerResponse;

import java.util.Date;

public interface LogService {

    Integer doSystemLogSave();

    PageResponse<SysLoggerResponse> list(int page, int limit, Date start, Date end, Long roleID);
}
