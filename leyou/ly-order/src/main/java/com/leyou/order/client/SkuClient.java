package com.leyou.order.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/6/1 14:38
 */
@FeignClient("item-service")
public interface SkuClient extends GoodsApi {
}
