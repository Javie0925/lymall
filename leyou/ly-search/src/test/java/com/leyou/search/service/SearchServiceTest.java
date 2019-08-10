package com.leyou.search.service;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.SpuBo;
import com.leyou.search.client.SpuClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author javie
 * @date 2019/5/23 0:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceTest {

    @Autowired
    private SearchService searchService;
    @Autowired
    private SpuClient spuClient;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testSearchService(){
        int page = 1;
        int rows = 200;
        PageResult<SpuBo> pageResult = spuClient.querySpuByPageAndSort(page, rows, "true", null, true);
        List<SpuBo> spuBoList = pageResult.getItems();
        for (SpuBo spuBo : spuBoList) {
            try {
                Goods goods = searchService.buildGoods(spuBo);
                if (goods!=null) {
                    goodsRepository.save(goods);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}