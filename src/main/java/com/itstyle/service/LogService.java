package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.SysLogger;

public interface LogService {

    Long doSystemLogSave();

    PageResponse<SysLogger> getAll(int page, int size);
}
