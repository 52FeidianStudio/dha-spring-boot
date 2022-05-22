package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;

/**
 * @author xunjiu
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    //private static final ThreadLocal<DataSourceRoleEnum> THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER =
    //    ThreadLocal.withInitial(() -> DataSourceRoleEnum.MASTER);
    private static final ThreadLocal<DataSourceRoleEnum> THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER = null;
    private static DataSourceRoleEnum globalDataSourceContextHolder = null;

    public static DataSourceRoleEnum getGlobalDataSourceRole() {
        log.info("get global data source role:{}", globalDataSourceContextHolder.name());
        return globalDataSourceContextHolder;
    }

    public static void setGlobalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set global data source role:{}", dataSourceRoleEnum.name());
            globalDataSourceContextHolder = dataSourceRoleEnum;
            THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("global data source role null");
    }

    public static DataSourceRoleEnum getThreadLocalDataSourceRole() {
        DataSourceRoleEnum dataSourceRoleEnum = THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get();
        if (dataSourceRoleEnum != null) {
            log.info("get thread local data source role:{}", dataSourceRoleEnum.name());
            return dataSourceRoleEnum;
        }
        log.error("get thread local data source role null,return master");
        return DataSourceRoleEnum.MASTER;
    }

    public static void setThreadLocalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set thread local data source role:{}", dataSourceRoleEnum.name());
            THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("thread local data source role null");
    }

    public static void threadLocalClear() {
        THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.remove();
    }

}