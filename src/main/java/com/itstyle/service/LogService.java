package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.log.SysLogger;

public interface LogService {

    Integer doSystemLogSave();

    PageResponse<SysLogger> list(int page, int limit);
}
