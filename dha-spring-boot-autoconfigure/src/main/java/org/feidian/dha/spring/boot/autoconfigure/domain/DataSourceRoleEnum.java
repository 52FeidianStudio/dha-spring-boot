package org.feidian.dha.spring.boot.autoconfigure.domain;

/**
 * @author xunjiu
 * @date 2022/3/24 16:39
 **/
public enum DataSourceRoleEnum {
    /**
     * 主数据源(默认读写)
     */
    MASTER,
    /**
     * 备数据源(默认只读)
     */
    STAND_BY,
}
