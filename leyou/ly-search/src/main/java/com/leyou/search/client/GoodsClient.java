package com.leyou.search.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/22 20:42
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
