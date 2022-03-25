package org.feidian.dha.spring.boot.autoconfigure;

import java.util.Map;
import java.util.Properties;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.config.DatasourceMapConverter;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSource;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.feidian.dha.spring.boot.autoconfigure.route.DynamicDataSourceContextHolder;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;

/**
 * @author xunjiu
 * @date 2022/3/24 10:23
 **/
@Slf4j
public class DhaService {
    private static final String DATA_ID = "org.feidian.dha.properties";
    private final DhaProperties dhaProperties;

    public DhaService(DhaProperties dhaProperties) {
        this.dhaProperties = dhaProperties;
    }

    @NacosConfigListener(dataId = DATA_ID, converter = DatasourceMapConverter.class)
    public void getRoleAndSetDataSource(Map<String, DataSource> dataSourceMap) {
        log.info("map:{}", dataSourceMap);
        DataSource dataSource = dataSourceMap.get(dhaProperties.getAppName());
        if (dataSource != null) {
            log.info("data source changed,new data:{}", dataSource);
            DynamicDataSourceContextHolder.setDataSourceRole(dataSource.getCurrentRole());
            return;
        }
        log.info("data source changed null map:{}", dataSourceMap);
    }

    @SneakyThrows
    public void getRoleAndSetDataSource(String appName) {
        String serverAddr = "127.0.0.1:8848";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String config = configService.getConfig(DATA_ID, DEFAULT_GROUP, 5000);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, DataSource> dataSourceMap = objectMapper.readValue(config,
            new TypeReference<Map<String, DataSource>>() {});
        log.info("data source map:{}", dataSourceMap);
        DataSource dataSource = dataSourceMap.get(dhaProperties.getAppName());
        DynamicDataSourceContextHolder.setDataSourceRole(dataSource.getCurrentRole());
    }
}
