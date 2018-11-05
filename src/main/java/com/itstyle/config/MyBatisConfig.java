package com.itstyle.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.itstyle.mapper")
public class MyBatisConfig {
}
