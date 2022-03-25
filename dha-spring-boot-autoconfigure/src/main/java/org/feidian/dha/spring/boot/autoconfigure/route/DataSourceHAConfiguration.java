package org.feidian.dha.spring.boot.autoconfigure.route;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties.DhaDataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author xunjiu
 * @date 2022/3/25 12:32
 **/
@Configuration
public class DataSourceHAConfiguration {
    @Resource
    private DhaProperties dhaProperties;

    @Bean(name = "standbyDataSource")
    public DataSource standbyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DhaDataSourceProperties standby = dhaProperties.getStandby();
        dataSource.setUrl(standby.getJdbcUrl());
        dataSource.setUsername(standby.getUserName());
        dataSource.setPassword(standby.getPassword());
        return dataSource;
    }

    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DhaDataSourceProperties master = dhaProperties.getMaster();
        dataSource.setUrl(master.getJdbcUrl());
        dataSource.setUsername(master.getUserName());
        dataSource.setPassword(master.getPassword());
        return dataSource;
    }

}
