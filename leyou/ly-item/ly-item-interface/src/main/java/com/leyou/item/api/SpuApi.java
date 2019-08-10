package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author javie
 * @date 2019/5/20 20:52
 */

@RequestMapping("spu")
public interface SpuApi {


    @GetMapping("page")
    PageResult<SpuBo> querySpuByPageAndSort(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value="saleable",required = false) String saleable,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value="desc",defaultValue = "false") Boolean desc

    );
    @GetMapping("detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable(value = "id") Long id);
    @GetMapping("{id}")
    Spu querySpuById(@PathVariable("id") Long id);
}
