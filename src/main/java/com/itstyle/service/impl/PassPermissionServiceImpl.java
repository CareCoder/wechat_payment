package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.caryard.EquipmentStatus;
import com.itstyle.domain.caryard.PassCarStatus;
import com.itstyle.exception.BusinessException;
import com.itstyle.mapper.EquipmentStatusMapper;
import com.itstyle.mapper.PassPermissionMapper;
import com.itstyle.service.PassPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PassPermissionServiceImpl implements PassPermissionService {

    private PassPermissionMapper passPermissionMapper;
    private EquipmentStatusMapper equipmentStatusMapper;

    @Autowired
    public PassPermissionServiceImpl(PassPermissionMapper passPermissionMapper,
                                     EquipmentStatusMapper equipmentStatusMapper) {
        this.passPermissionMapper = passPermissionMapper;
        this.equipmentStatusMapper = equipmentStatusMapper;
    }

    @Override
    public List<PassCarStatus> list() {
        return passPermissionMapper.findAll();
    }

    @Override
    public void update(List<PassCarStatus> lists) {
        List<PassCarStatus> save = passPermissionMapper.save(lists);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
    }

    @Override
    public PageResponse<EquipmentStatus> equipmentList(int page, int limit) {
        return PageResponse.build(equipmentStatusMapper.findAll(PageResponse.getPageRequest(page, limit)));
    }
}
