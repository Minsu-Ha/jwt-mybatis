package com.example.combine.test;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private TestService service;

    @GetMapping("/test")
    public String test() throws SQLException {


        return "test: " + service.test();

    }
}
