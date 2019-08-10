package com.leyou.search.client;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/22 23:45
 */
@FeignClient("item-service")
public interface SpecificationClient extends com.leyou.item.api.SpecificationApi {
}
