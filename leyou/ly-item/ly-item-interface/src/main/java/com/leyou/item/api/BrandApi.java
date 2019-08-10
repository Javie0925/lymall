package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/19 13:09
 */
@RequestMapping("/brand")
public interface BrandApi {


    @GetMapping("/page")
    PageResult<Brand> queryBrandByPage(
            @RequestParam(value="page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    );


    @GetMapping("/cid/{cid}")
    List<Brand> queryBrandByCid(@PathVariable("cid") Long cid);
    @GetMapping("{id}")
    Brand queryBrandByBid(@PathVariable("id") Long id);
    @GetMapping("list")
    List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);
}
