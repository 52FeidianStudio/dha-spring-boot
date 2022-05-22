//package org.feidian.dha.spring.boot.autoconfigure.route;
//
//import java.util.Arrays;
//import java.util.Properties;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.plugin.Intercepts;
//import org.apache.ibatis.plugin.Invocation;
//import org.apache.ibatis.plugin.Plugin;
//import org.apache.ibatis.plugin.Signature;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
//import org.springframework.stereotype.Service;
//
///**
// * @author xunjiu
// * @date 2022/5/9 10:40
// **/
//@Slf4j
//@Intercepts({
//    @Signature(type = Executor.class, method = "query",
//        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//    @Signature(type = Executor.class, method = "query",
//        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql
//        .class}),
//    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
//@Service
//public class MyBatisInterceptor implements Interceptor {
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        log.info("into mybatis interceptor: {}",invocation);
//        Object[] args = invocation.getArgs();
//        MappedStatement ms = (MappedStatement)args[0];
//        DataSourceRoleEnum dataSourceRole = null;
//        try {
//            boolean canBeStandby = SqlCommandType.SELECT == ms.getSqlCommandType();
//
//            // 查询全局主备节点状态
//            DataSourceRoleEnum master = DynamicDataSourceContextHolder.getGlobalDataSourceRole();
//            DataSourceRoleEnum standBy = Arrays.stream(DataSourceRoleEnum.values())
//                .filter(item -> !item.equals(master)).findFirst()
//                .orElseThrow(() -> new RuntimeException("error get data source role"));
//
//            dataSourceRole = canBeStandby ? standBy : master;
//            // 如果可以路由到备节点，即 读 操作，再判断同地域优先
//            // 此操作好处在于，第一减少跨地域读，提高读的性能；第二如果备库地域故障，路由到备库读，那么所有读操作都会失败
//            // todo
//            if (canBeStandby) {
//                // 如果当前 region 和 master region 相同，则本地读
//                // 如果当前 region 和 master region 不同，则本地读
//            }
//
//            DynamicDataSourceContextHolder.setThreadLocalDataSourceRole(dataSourceRole);
//            System.out.println("mybatis interceptor");
//            log.error("mybatis interceptor" );
//            return invocation.proceed();
//        } finally {
//            log.info("global data source:{},threadLocal data source:{}",
//                DynamicDataSourceContextHolder.getGlobalDataSourceRole()
//                , DynamicDataSourceContextHolder.getThreadLocalDataSourceRole());
//            if (dataSourceRole == null) {
//                log.error("set threadLocal data source error,data source role is null");
//            }
//            DynamicDataSourceContextHolder.threadLocalClear();
//        }
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        //Interceptor.super.setProperties(properties);
//    }
//}
