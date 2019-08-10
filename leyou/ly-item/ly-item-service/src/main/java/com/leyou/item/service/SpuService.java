package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpuDetailsMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javie
 * @date 2019/5/20 20:26
 */
@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuDetailsMapper spuDetailsMapper;

    public PageResult<SpuBo> querySpuByPageAndSort(
            Integer page, Integer rows, Boolean saleable, String key, Boolean desc) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(saleable!=null){
            criteria.andEqualTo("saleable", saleable);
        }
        if(StringUtils.isNoneBlank(key)){
            criteria.andLike("title", "%"+key+"%");
        }
        //排序
        String orderByClause = "id " + (desc?"desc":"asc");
        example.setOrderByClause(orderByClause);
        //查询
        List<Spu> spuList = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(spuList)){
            return new PageResult<SpuBo>(0L,null);
        }
        List<SpuBo> spuBoList = new ArrayList<>();
        //分装spubo
        for (Spu spu:spuList){
           List<String> names = categoryService.selectByIds(spu.getCid1(),spu.getCid2(),spu.getCid3());
           String cname = StringUtils.join(names, "/");
           String bname = brandMapper.selectByPrimaryKey(spu.getBrandId()).getName();
           spuBoList.add(new SpuBo(spu,cname,bname));
        }
        PageInfo<Spu> pageInfo = new PageInfo(spuList);
        return new PageResult<SpuBo>(pageInfo.getTotal(),spuBoList);
    }

    public SpuDetail querySpuDetailById(Long id) {

        SpuDetail spuDetail = spuDetailsMapper.selectByPrimaryKey(id);
        return spuDetail;
    }

    public Spu querySpuById(Long id) {

        Spu spu = spuMapper.selectByPrimaryKey(id);
        return spu;
    }
}
