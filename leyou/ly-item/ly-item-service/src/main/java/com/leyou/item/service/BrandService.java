package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javie
 * @date 2019/5/19 13:32
 */

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key){

        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().
                    orLike("name", "%"+key+"%").
                    orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?"desc":"asc");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> brands = brandMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(brands)){
           throw new LyException(ExceptionEnmu.BRAND_NOT_FOUND);
        }
        PageInfo<Brand> pageInfo = new PageInfo<Brand>(brands);

        return new PageResult<Brand>(pageInfo.getTotal(),brands);
    }

    @Transactional    // 事务
    public void saveBrand(Brand brand, List<Long> categories) {
        //新增品牌
        int count = brandMapper.insert(brand);
        if(count!=1){
            throw new LyException(ExceptionEnmu.BRAND_SAVE_ERROR);
        }
        // 中间表
        for(Long cid:categories){
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count==0){
                throw new LyException(ExceptionEnmu.BRAND_SAVE_ERROR);
            }

        }
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brands = new ArrayList<>();
        List<Long> bids = brandMapper.queryBidByCid(cid);
        if (CollectionUtils.isEmpty(bids)){
            return null;
        }
        //System.out.println(ArrayUtils.toString(bids));
        //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for (Long bid:bids){
            Brand brand = brandMapper.selectByPrimaryKey(bid);
            if(brand!=null) {
                brands.add(brand);
            }
        }
        return brands;
    }

    public Brand queryBrandByBid(Long id) {

        return brandMapper.selectByPrimaryKey(id);
    }

    public List<Brand> queryBrandByBids(List<Long> ids) {

        List<Brand> brandList = brandMapper.selectByIdList(ids);
        return brandList;
    }
}
