package com.example.mall.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.example.mall.mapper", "com.example.mall.search.dao"})
public class MyBatisConfig {
}
