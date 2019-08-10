package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author javie
 * @date 2019/5/21 10:41
 */
public interface SkuMapper extends Mapper<Sku>,IdListMapper<Sku,Long> {
}
