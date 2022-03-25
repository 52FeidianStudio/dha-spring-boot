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

    @Data
    public class DhaDataSourceProperties {
        /**
         * jdbc 连接串
         */
        private String jdbcUrl;
        /**
         * 用户名
         */
        private String userName;
        /**
         * 密码
         */
        private String password;
        /**
         * 高权限账户名
         */
        private String adminName;
        /**
         * 高权限用户密码
         */
        private String adminPassword;
    }
}
