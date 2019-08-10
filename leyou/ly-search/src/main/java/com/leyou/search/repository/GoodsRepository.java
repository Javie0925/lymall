package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author javie
 * @date 2019/5/22 21:11
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
