package com.itstyle.service.impl;

import com.itstyle.domain.account.Account;
import com.itstyle.domain.caryard.PassCarStatus;
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

    @Autowired
    public PassPermissionServiceImpl(PassPermissionMapper passPermissionMapper) {
        this.passPermissionMapper = passPermissionMapper;
    }

    @Override
    public List<PassCarStatus> list() {
        return passPermissionMapper.findAll();
    }

    @Override
    public void update(List<PassCarStatus> lists) {

    }
}
