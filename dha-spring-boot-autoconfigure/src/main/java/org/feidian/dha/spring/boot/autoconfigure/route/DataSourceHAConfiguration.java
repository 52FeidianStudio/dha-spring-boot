package org.feidian.dha.spring.boot.autoconfigure.route;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSourceProperties;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author xunjiu
 * @date 2022/3/25 12:32
 **/
@Configuration
@Slf4j
public class DataSourceHAConfiguration {
    @Resource
    private DhaProperties dhaProperties;

    @Bean(name = "standbyDataSourceSUD")
    public DataSource standbyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DhaDataSourceProperties standby = dhaProperties.getStandby();
        log.info("config properties:{}", dhaProperties);
        dataSource.setUrl(standby.getJdbcUrl());
        dataSource.setUsername(standby.getUserName());
        dataSource.setPassword(standby.getPassword());
        log.info("standby datasource:{}", dataSource);
        return dataSource;
    }

    @Bean(name = "masterDataSourceSUD")
    @Primary
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.info("config properties:{}", dhaProperties);
        DhaDataSourceProperties master = dhaProperties.getMaster();
        dataSource.setUrl(master.getJdbcUrl());
        dataSource.setUsername(master.getUserName());
        dataSource.setPassword(master.getPassword());
        log.info("master datasource:{}", dataSource);
        return dataSource;
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("masterDataSourceSUD") DataSource masterDataSource,
        @Qualifier("standbyDataSourceSUD") DataSource standbyDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        log.info("param:master:{},standby:{}", masterDataSource, standbyDataSource);
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceRoleEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceRoleEnum.STAND_BY, standbyDataSource);
        routingDataSource.setDefaultTargetDataSource(standbyDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    @Bean
    SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer(
        @Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return factoryBean -> {
            factoryBean.setDataSource(dynamicDataSource);
        };
    }
}