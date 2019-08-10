package com.leyou.order.mapper;

import com.leyou.order.pojo.OrderDetail;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author javie
 * @date 2019/6/1 16:17
 */
public interface OrderDetailMapper extends Mapper<OrderDetail>,IdListMapper<OrderDetail,Long> {
}
