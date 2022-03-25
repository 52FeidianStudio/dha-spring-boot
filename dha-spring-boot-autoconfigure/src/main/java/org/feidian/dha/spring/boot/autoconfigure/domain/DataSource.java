package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xunjiu
 * @date 2022/3/24 17:31
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    /**
     * 当前角色
     */
    private DataSourceRoleEnum currentRole;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 主库（读写）jdbc 连接串
     */
    private String masterDBConnection;

    /**
     * 备库（只读）jdbc 连接串
     */
    private String standbyDBConnection;
}
