package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xunjiu
 * @date 2022/3/24 10:23
 **/
@Data
@ConfigurationProperties("dha.config")
public class DhaProperties {
    private DhaDataSourceProperties master;
    private DhaDataSourceProperties standby;
    private String appName;

}
