package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;

public interface PassPermissionService {

    PageResponse<ResponsePassCarStatus> list(int page, int limit);

    void save(PassCarStatus passCarStatus);

    void update(PassCarStatus passCarStatus);

    void delete(Long id);

    PassCarStatus getById(Long id);

}
