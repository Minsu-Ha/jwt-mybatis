package com.example.combine.test;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class TestDao {
    public String test(SqlSession primary) {
        return primary.selectOne("primaryTestMapper.selectTest");
    }
}
