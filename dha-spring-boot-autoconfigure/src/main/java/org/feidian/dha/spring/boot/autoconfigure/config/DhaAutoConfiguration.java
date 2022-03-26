package org.feidian.dha.spring.boot.autoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.DhaService;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xunjiu
 * @date 2022/3/24 10:22
 **/
@Configuration
@ConditionalOnClass(DhaService.class)
@EnableConfigurationProperties(DhaProperties.class)
@Slf4j
public class DhaAutoConfiguration {
    private final DhaProperties dhaProperties;

    public DhaAutoConfiguration(DhaProperties dhaProperties) {this.dhaProperties = dhaProperties;}

    @Bean
    @ConditionalOnMissingBean(DhaService.class)
    @ConditionalOnProperty(prefix = "dha.config", value = "enable", havingValue = "true")
    DhaService dhaService() {
        return new DhaService(dhaProperties);
    }
}
