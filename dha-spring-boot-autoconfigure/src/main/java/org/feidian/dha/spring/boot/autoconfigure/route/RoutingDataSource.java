package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author xunjiu
 * @date 2022/3/24 16:42
 **/
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceRoleEnum dataSourceRoleEnum = DynamicDataSourceContextHolder.getThreadLocalDataSourceRole();
        log.info("query data source:{}", dataSourceRoleEnum);
        return dataSourceRoleEnum;
    }
}
