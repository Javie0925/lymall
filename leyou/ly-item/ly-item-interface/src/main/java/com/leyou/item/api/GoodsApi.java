package com.leyou.item.api;

import com.leyou.item.pojo.Sku;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/22 19:53
 */
@RequestMapping
public interface GoodsApi {


    @GetMapping("goods/sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("/sku/list/ids")
    List<Sku> querySkuByIds(@RequestParam("ids")List<Long> ids);
}
