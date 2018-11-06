package com.itstyle.service;

import com.itstyle.domain.account.Role;

import java.util.List;

public interface RoleService {

    Role insert(Role role);

    void delete(List<Long> ids);
}
