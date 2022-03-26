package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunjiu
 * @date 2022/3/26 01:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
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
