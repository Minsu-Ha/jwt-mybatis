package com.example.combine.admin.dataSource.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Data
@Slf4j
public class DataSourceUtil {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Resource[] mapperLocation;

    private DataSource dataSource;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    private DataSourceTransactionManager transactionManager;

    public DataSourceUtil(Builder builder) throws Exception {
        this.driverClassName = builder.driverClassName;
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.mapperLocation = builder.mapperLocation;

        DriverManagerDataSource dds = new DriverManagerDataSource();

        dds.setDriverClassName(driverClassName);
        dds.setUrl(url);
        dds.setUsername(username);
        dds.setPassword(password);

        this.dataSource = dds;

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(this.dataSource);
        sqlSessionFactory
                .setMapperLocations(this.mapperLocation);

        this.sqlSessionFactory = sqlSessionFactory.getObject();

        this.sqlSessionTemplate = new SqlSessionTemplate(this.sqlSessionFactory);

        this.transactionManager = new DataSourceTransactionManager(this.dataSource);

    }

    public static class Builder {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private Resource[] mapperLocation;

        public Builder driverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder mapperLocation(Resource[] mapperLocation) {
            this.mapperLocation = mapperLocation;
            return this;
        }

        public DataSourceUtil build() throws Exception {
            return new DataSourceUtil(this);
        }
    }

}
