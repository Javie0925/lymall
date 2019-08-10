package com.leyou.page.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/22 20:44
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
