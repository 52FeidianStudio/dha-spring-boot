package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;

/**
 * @author xunjiu
 */
@Slf4j
public class DynamicDataSourceContextHolder {

    private static DataSourceRoleEnum contextHolder = DataSourceRoleEnum.MASTER;

    public static DataSourceRoleEnum getDataSourceRole() {
        return contextHolder;
    }

    public static void setDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set data source role:{}", dataSourceRoleEnum.name());
            contextHolder = dataSourceRoleEnum;
            return;
        }
        log.error("data source role null");
    }

}