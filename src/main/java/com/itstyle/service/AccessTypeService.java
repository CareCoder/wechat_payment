package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.AccessType;
import com.itstyle.domain.caryard.ChannelType;
import com.itstyle.domain.caryard.ResponseAccessType;

import java.util.List;

public interface AccessTypeService {

    PageResponse<ResponseAccessType> list(int page, int limit);
    List<ResponseAccessType> listNoPage();

    void save(AccessType accessType);

    void edit(AccessType accessType);

    AccessType delete(Long id);

    AccessType getById(Long id);

    List<ChannelType> getAllChannelType();
}
