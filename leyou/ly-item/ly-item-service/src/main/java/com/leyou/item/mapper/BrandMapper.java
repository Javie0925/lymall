package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 品牌查询mapper
 * @author javie
 * @date 2019/5/19 13:07
 */
public interface BrandMapper extends Mapper<Brand> ,IdsMapper<Brand>,IdListMapper<Brand,Long>{

    @Insert("INSERT INTO tb_category_brand VALUES(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid,@Param("bid")Long bid);

    @Select(value = "SELECT brand_id FROM tb_category_brand where category_id=#{cid}")
    List<Long> queryBidByCid(Long cid);

}
