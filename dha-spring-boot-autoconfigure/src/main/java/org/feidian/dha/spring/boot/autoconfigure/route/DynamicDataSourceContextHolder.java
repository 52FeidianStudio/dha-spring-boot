package org.feidian.dha.spring.boot.autoconfigure.route;

import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;

/**
 * @author xunjiu
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<DataSourceRoleEnum> THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER = new ThreadLocal<>();
    private static final AtomicReference<DataSourceRoleEnum> GLOBAL_DATA_SOURCE_CONTEXT_HOLDER = new AtomicReference<>();

    private static final ThreadLocal<String> type = new ThreadLocal<>();

    public static DataSourceRoleEnum getGlobalDataSourceRole() {
        log.info("get global data source role:{}", GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.get().name());
        return GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.get();
    }

    public static void setGlobalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set global data source role:{}", dataSourceRoleEnum.name());
            GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("global data source role null");
    }

    public static DataSourceRoleEnum getThreadLocalDataSourceRole() {
        DataSourceRoleEnum dataSourceRoleEnum = THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get();
        if (dataSourceRoleEnum != null) {
            log.info("get thread local data source role:{}", dataSourceRoleEnum.name());
            /**
             * 用过之后立刻删除，防止线程复用，导致最终核心线程都路由到 master 节点
             */
            THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.remove();
            return dataSourceRoleEnum;
        }
        return null;
    }

    public static void setThreadLocalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set thread local data source role:{}", dataSourceRoleEnum.name());
            // 如果走到主节点，不允许覆盖，这样会重新回到从节点
            // 对应场景：In a transaction,first write then read,data inconsistency
            //if(dataSourceRoleEnum.equals(DataSourceRoleEnum.STAND_BY) &&
            //DataSourceRoleEnum.MASTER.name().equals(THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get().name()) &&
            //    DataSourceRoleEnum.MASTER.name().equals(RegionRoleContextHolder.getCurrentRegionRole().name())){
            //    return ;
            //}
            //if(THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get().equals(DataSourceRoleEnum.MASTER))
            THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("thread local data source role null");
    }

    public static void threadLocalClear() {
        log.info("thread local data source {} is clear", THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get());
        THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.remove();
    }

}