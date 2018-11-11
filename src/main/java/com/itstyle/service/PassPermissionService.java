package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.PassCarStatus;

import java.util.List;

public interface PassPermissionService {

    List<PassCarStatus> list();

    void update(List<PassCarStatus> lists);

    PageResponse<EquipmentStatus> equipmentList(int page, int limit);
}
