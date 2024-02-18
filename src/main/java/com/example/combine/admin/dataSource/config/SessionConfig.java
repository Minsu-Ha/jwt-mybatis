package com.example.combine.admin.dataSource.config;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SessionConfig {

    @Bean
    public Map<String, SqlSessionTemplate> sessionMap() {
        return new HashMap<>();
    }

}
