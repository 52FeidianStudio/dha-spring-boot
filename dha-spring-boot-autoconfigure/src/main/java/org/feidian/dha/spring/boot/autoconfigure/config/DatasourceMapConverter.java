package org.feidian.dha.spring.boot.autoconfigure.config;

import java.util.Map;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSource;

/**
 * @author xunjiu
 * @date 2022/3/24 18:30
 **/
@Slf4j
public class DatasourceMapConverter implements NacosConfigConverter<Map<String, DataSource>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canConvert(Class<Map<String, DataSource>> targetClass) {
        boolean canConvert = objectMapper.canSerialize(targetClass);
        if (canConvert) {
            log.info("can convert:{}", canConvert);
            System.out.println("can convert");
        } else {
            log.error("can convert:{}", canConvert);
            System.out.println("can not convert");
        }
        return canConvert;
    }

    @SneakyThrows
    @Override
    public Map<String, DataSource> convert(String config) {
        return objectMapper.readValue(config, new TypeReference<Map<String, DataSource>>() {});
    }
}
