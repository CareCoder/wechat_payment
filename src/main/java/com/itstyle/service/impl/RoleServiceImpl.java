package com.itstyle.service.impl;

import com.itstyle.domain.account.Role;
import com.itstyle.mapper.RoleMapper;
import com.itstyle.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Role insert(Role role) {
        return roleMapper.save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Long> ids) {
        ids.forEach(id -> roleMapper.delete(id));
        log.info("[RoleServiceImpl] delete role ids [{}] size [{}]", ids, ids.size());
    }
}
