package com.leyou.search.client;

import com.leyou.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/22 20:43
 */
@FeignClient("item-service")
public interface SpuClient extends SpuApi {
}
