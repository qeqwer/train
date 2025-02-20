package com.niko.train.common.util;


import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

/**
 * 封装hutool雪花算法
 **/
@Component
public class SnowUtil {
    private static long dataCenterId = 1;
    private static long workerId  = 1;

    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }

}
