package com.example.combine.admin.dataSource.config;

import com.example.combine.admin.dataSource.common.util.DataSourceUtil;
import com.zaxxer.hikari.util.DriverDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "spring.datasource")
public class PrimaryDataSourceConfig {


    private Map<String, String> primary;

    private DataSourceUtil dataSourceUtil;


    public void setPrimary(Map<String, String> primary) {
        this.primary = primary;
    }

    @Bean(name = "primaryDataSource")
    public DataSource primaryDataSource() throws Exception {

        dataSourceUtil = new DataSourceUtil
                .Builder()
                .driverClassName(primary.get("driver-class-name"))
                .url(primary.get("url"))
                .username(primary.get("username"))
                .password(primary.get("password"))
                .mapperLocation(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/primary/**/*.xml"))
                .build();
        return dataSourceUtil.getDataSource();
    }

    @Bean(name = "primarySqlSessionFactory")
    @DependsOn("primaryDataSource")
    public SqlSessionFactory primarySqlSessionFactory() {
        return dataSourceUtil.getSqlSessionFactory();
    }

    @Bean(name = "primary")
    @DependsOn("primaryDataSource")
    public SqlSessionTemplate primarySqlSessionTemplate(Map<String, SqlSessionTemplate> sessionMap) {
        SqlSessionTemplate session = dataSourceUtil.getSqlSessionTemplate();
        sessionMap.put("primary", session);
        return session;
    }

    @Bean(name= "primaryTransactionManager")
    @DependsOn("primaryDataSource")
    public DataSourceTransactionManager transactionManager() {
        return dataSourceUtil.getTransactionManager();
    }



}
