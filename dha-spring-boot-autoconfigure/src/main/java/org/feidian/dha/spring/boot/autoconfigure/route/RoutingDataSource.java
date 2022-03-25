package org.feidian.dha.spring.boot.autoconfigure.route;

import java.util.HashMap;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author xunjiu
 * @date 2022/3/24 16:42
 **/
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceRole();
    }

    public void initDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
        @Qualifier("standbyDataSource") DataSource standbyDataSource) {
        log.info("param:master:{},standby:{}", masterDataSource, standbyDataSource);
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceRoleEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceRoleEnum.STAND_BY, standbyDataSource);
        this.setTargetDataSources(targetDataSources);
        this.setDefaultTargetDataSource(masterDataSource);
    }
}
