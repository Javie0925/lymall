package com.leyou.page.service;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.*;
import com.leyou.page.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author javie
 * @date 2019/5/24 21:10
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private SpuClient spuClient;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public Map<String, Object> loadModel(Long spuId){

        try {
            // 查询spu
            Spu spu = this.spuClient.querySpuById(spuId);

            // 查询spu详情
            SpuDetail spuDetail = this.spuClient.querySpuDetailById(spuId);

            // 查询sku
            List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

            // 查询品牌
            List<Brand> brands = this.brandClient.queryBrandByIds(Arrays.asList(spu.getBrandId()));

            // 查询分类
            List<Category> categories = getCategories(spu);

            // 查询组内参数
//            List<SpecGroup> specGroups = this.specificationClient.querySpecsByCid(spu.getCid3());

            // 查询所有特有规格参数
//            List<SpecParam> specParams = this.specificationClient.querySpecParam(null, spu.getCid3(), null, false);
            // 处理规格参数
//            Map<Long, String> paramMap = new HashMap<>();
//            specParams.forEach(param->{
//                paramMap.put(param.getId(), param.getName());
//            });
            String specTemplate = spuDetail.getSpecTemplate();
            Map<String, ArrayList> specTemplateMap = JsonUtils.toMap(specTemplate, String.class, ArrayList.class);
            System.out.println("?????????????????????????????????????????");
            System.out.println(specTemplateMap);
            String specParamStr = spuDetail.getSpecifications();
            List<SpecParam> specParamList = JsonUtils.toList(specParamStr, SpecParam.class);
            Map<String, Object> map = new HashMap<>();
            map.put("spu", spu);
            map.put("spuDetail", spuDetail);
            map.put("skus", skus);
            map.put("sku", skus.get(0));
            map.put("brand", brands.get(0));
            map.put("categories", categories);
            map.put("specTemplateMap", specTemplateMap);
            map.put("specParamList", specParamList);
            return map;
        } catch (Exception e) {
            logger.error("加载商品数据出错,spuId:{}", spuId, e);
        }
        return null;
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.queryNameByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Category c1 = new Category();
            c1.setName(names.get(0));
            c1.setId(spu.getCid1());

            Category c2 = new Category();
            c2.setName(names.get(1));
            c2.setId(spu.getCid2());

            Category c3 = new Category();
            c3.setName(names.get(2));
            c3.setId(spu.getCid3());

            return Arrays.asList(c1, c2, c3);
        } catch (Exception e) {
            logger.error("查询商品分类出错，spuId：{}", spu.getId(), e);
        }
        return null;
    }
}
