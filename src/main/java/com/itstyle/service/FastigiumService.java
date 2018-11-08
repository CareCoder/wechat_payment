package com.itstyle.service;

import com.alibaba.fastjson.JSON;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import com.itstyle.domain.fastigium.Fastigium;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class FastigiumService {
    @Resource
    private RedisDao redisDao;

    private static final String FASTIGIUM_KEY = "FASTIGIUM_KEY";

    public Fastigium get() {
        Fastigium fastigium;
        try {
            String hget = redisDao.hget(YstCommon.CONFIGURE_FIELD, FASTIGIUM_KEY);
            fastigium = JSON.parseObject(hget, Fastigium.class);
            if (fastigium == null) {
                return Fastigium.buildDefault();
            }
        } catch (Exception e) {
            log.error("[FastigiumService] get error", e);
            return Fastigium.buildDefault();
        }
        return fastigium;
    }

    public void set(Fastigium fastigium) {
        redisDao.hset(YstCommon.CONFIGURE_FIELD, FASTIGIUM_KEY, JSON.toJSONString(fastigium));
    }
}
