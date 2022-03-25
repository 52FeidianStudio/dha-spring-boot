package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;

/**
 * @author xunjiu
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<DataSourceRoleEnum> CONTEXT_HOLDER =
        ThreadLocal.withInitial(() -> DataSourceRoleEnum.MASTER);

    public static DataSourceRoleEnum getDataSourceRole() {
        DataSourceRoleEnum dataSourceRoleEnum = CONTEXT_HOLDER.get();
        log.info("get data source role:{}", dataSourceRoleEnum.name());
        return dataSourceRoleEnum;
    }

    public static void setDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set data source role:{}", dataSourceRoleEnum.name());
            CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("data source role null");
    }

    public static void clearDataSourceRole() {
        CONTEXT_HOLDER.remove();
    }
}
