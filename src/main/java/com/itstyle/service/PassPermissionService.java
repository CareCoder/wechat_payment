package com.itstyle.service;

import com.itstyle.domain.caryard.PassCarStatus;

import java.util.List;

public interface PassPermissionService {

    List<PassCarStatus> list();

    void update(List<PassCarStatus> lists);
}
