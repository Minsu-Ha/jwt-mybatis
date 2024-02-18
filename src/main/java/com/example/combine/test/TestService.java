package com.example.combine.test;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {

    @Autowired
    private Map<String, SqlSessionTemplate> sessionMap;

    @Autowired
    private TestDao dao;

    public String test() {


        return dao.test((SqlSession)sessionMap.get("primary"));
    }
}
