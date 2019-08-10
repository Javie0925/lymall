package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author javie
 * @date 2019/6/1 15:33
 */
@ConfigurationProperties(prefix = "ly.worker")
@Data
public class IdWorkerProperties {
    private long workerId;
    private long dataCenterId;
}
