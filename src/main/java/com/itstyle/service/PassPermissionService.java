package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.domain.caryard.ResponsePassCarStatus;

import java.util.List;

public interface PassPermissionService {

    PageResponse<ResponsePassCarStatus> list(int page, int limit);

    List<ResponsePassCarStatus> list();

    void save(PassCarStatus passCarStatus);

    void update(PassCarStatus passCarStatus);

    void delete(Long id);

    PassCarStatus getById(Long id);

    AccessType getAccessTypeId(Long id);

}
