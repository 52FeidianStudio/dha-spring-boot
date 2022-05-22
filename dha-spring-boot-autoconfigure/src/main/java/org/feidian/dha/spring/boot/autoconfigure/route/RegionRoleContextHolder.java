package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.RegionRoleEnum;

/**
 * @author xunjiu
 * @date 2022/5/22 21:33
 **/
@Slf4j
public class RegionRoleContextHolder {
    private static RegionRoleEnum currentRegionRole = null;

    public static RegionRoleEnum getCurrentRegionRole() {
        log.info("get region role:{}", currentRegionRole);
        return currentRegionRole;
    }

    public static void setCurrentRegionRole(RegionRoleEnum currentRegionRole) {
        if (currentRegionRole != null) {
            log.info("set region role:{}", currentRegionRole);
            RegionRoleContextHolder.currentRegionRole = currentRegionRole;
            return;
        }
        log.info("set region role null");
    }
}
