package com.itstyle.dao;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Repository
public class RedisDao {
    @Autowired
    private StringRedisTemplate template;

    private final RedisTemplate<Object, Object> templateObj;

    private static final Gson gson = new Gson();

    @Autowired
    public RedisDao(RedisTemplate<Object, Object> templateObj) {
        this.templateObj = templateObj;
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        FastJsonRedisSerializer<Object> jsonSerializer = new FastJsonRedisSerializer<>(Object.class);
        this.templateObj.setKeySerializer(stringSerializer);
        this.templateObj.setValueSerializer(jsonSerializer);
        this.templateObj.setHashKeySerializer(stringSerializer);
        this.templateObj.setHashValueSerializer(jsonSerializer);
    }

    public void set(String key, String value) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, value);
    }

    public String get(String key) {
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }

    public void hset(String filed, String key, String value) {
        HashOperations<String, String, String> ops = template.opsForHash();
        ops.put(filed, key, value);
    }

    public String hget(String filed, String key) {
        HashOperations<String, String, String> ops = template.opsForHash();
        return ops.get(filed, key);
    }

    public void hdelete(String filed, String key) {
        template.opsForHash().delete(filed, key);
    }

    public String execute(String lua, String keys, String... args) throws IOException {
        ClassPathResource resource = new ClassPathResource("lua/" + lua);
        InputStream inputStream = resource.getInputStream();
        StringBuilder luaCode = new StringBuilder();
        IOUtils.readLines(inputStream).forEach(luaCode::append);
        RedisScript<String> script = new DefaultRedisScript<>(luaCode.toString(), String.class);
        String execute = template.execute(script, Collections.singletonList(keys), args);
        return execute;
    }
}
