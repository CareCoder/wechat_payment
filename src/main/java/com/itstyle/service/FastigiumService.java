package com.itstyle.service;

import com.itstyle.domain.fastigium.Fastigium;
import com.itstyle.mapper.FastigiumMapper;
import com.itstyle.utils.hibernate.BaseDaoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FastigiumService extends BaseDaoService<Fastigium,Long> {
    @Resource
    private FastigiumMapper fastigiumMapper;

    public FastigiumService() {
        setJpaRepository(fastigiumMapper);
    }
}
