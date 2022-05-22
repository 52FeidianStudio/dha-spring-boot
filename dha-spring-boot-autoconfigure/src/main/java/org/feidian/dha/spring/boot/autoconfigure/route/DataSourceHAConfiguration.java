package org.feidian.dha.spring.boot.autoconfigure.route;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSourceProperties;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;
import static org.feidian.dha.spring.boot.autoconfigure.config.Constant.DATA_ID;

/**
 * @author xunjiu
 * @date 2022/3/25 12:32
 **/
@Configuration
@Slf4j
public class DataSourceHAConfiguration {
    @Resource
    private DhaProperties dhaProperties;
    @NacosInjected
    private ConfigService configService;

    @Bean(name = "standbyDataSource")
    public DataSource standbyDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DhaDataSourceProperties standby = dhaProperties.getStandby();
        if (standby == null) {
            throw new RuntimeException("config standby datasource is null");
        }
        log.info("config properties:{}", dhaProperties.getStandby());
        dataSource.setUrl(standby.getJdbcUrl());
        dataSource.setUsername(standby.getUserName());
        dataSource.setPassword(standby.getPassword());
        log.info("standby datasource:{}", dataSource);
        return dataSource;
    }

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        DhaDataSourceProperties master = dhaProperties.getMaster();
        if (master == null) {
            throw new RuntimeException("config standby datasource is null");
        }
        log.info("config properties:{}", dhaProperties.getMaster());
        dataSource.setUrl(master.getJdbcUrl());
        dataSource.setUsername(master.getUserName());
        dataSource.setPassword(master.getPassword());
        log.info("master datasource:{}", dataSource);
        return dataSource;
    }

    @SneakyThrows
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
        @Qualifier("standbyDataSource") DataSource standbyDataSource) {
        if (masterDataSource == null) {
            throw new RuntimeException("master data source is null");
        }
        if (standbyDataSource == null) {
            throw new RuntimeException("standby data source is null");
        }
        RoutingDataSource routingDataSource = new RoutingDataSource();
        log.info("param:master:{},standby:{}", masterDataSource, standbyDataSource);
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceRoleEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceRoleEnum.STAND_BY, standbyDataSource);
        String config = configService.getConfig(DATA_ID, DEFAULT_GROUP, 1000L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, DhaDataSource> map = objectMapper.readValue(config, new TypeReference<Map<String, DhaDataSource>>() {});
        DhaDataSource appDataSource = map.get(dhaProperties.getAppName());
        DataSource defaultDataSource;
        log.info("init appDataSource:{}", appDataSource);
        if (appDataSource != null && DataSourceRoleEnum.STAND_BY.equals(appDataSource.getCurrentRole())) {
            log.info("init dha standby");
            defaultDataSource = standbyDataSource;
            DynamicDataSourceContextHolder.setGlobalDataSourceRole(DataSourceRoleEnum.STAND_BY);
            DynamicDataSourceContextHolder.setThreadLocalDataSourceRole(DataSourceRoleEnum.STAND_BY);
        } else {
            log.info("init dha master");
            defaultDataSource = masterDataSource;
            DynamicDataSourceContextHolder.setGlobalDataSourceRole(DataSourceRoleEnum.MASTER);
            DynamicDataSourceContextHolder.setThreadLocalDataSourceRole(DataSourceRoleEnum.MASTER);
        }
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
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