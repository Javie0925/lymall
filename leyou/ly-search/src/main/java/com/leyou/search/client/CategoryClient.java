package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/22 15:29
 */

@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi{

}
