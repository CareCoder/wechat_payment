package com.itstyle.service;

import com.alibaba.fastjson.JSON;
import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class GlobalSettingService<T> {
    @Resource
    private RedisDao redisDao;

    public T get(String key, Class<T> clazz) {
        String hget = redisDao.hget(YstCommon.GLOBAL_SETTIING_FIELD, key);
        return JSON.parseObject(hget, clazz);
    }

    public void set(String key, Object param) {
        redisDao.hset(YstCommon.GLOBAL_SETTIING_FIELD, key, JSON.toJSONString(param));
    }
}
