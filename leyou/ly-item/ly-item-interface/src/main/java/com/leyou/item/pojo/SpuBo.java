package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class SpuBo extends Spu {

    @Transient
    private String categoryName;// 商品分类名称

    @Transient
    private String brandName;// 品牌名称

    public SpuBo(){}
    public SpuBo(Spu spu, String categoryName, String brandName){
        this.setBrandName(brandName);
        this.setCategoryName(categoryName);
        this.setId(spu.getId());
        this.setBrandId(spu.getBrandId());
        this.setCid1(spu.getCid1());
        this.setCid2(spu.getCid2());
        this.setCid3(spu.getCid3());
        this.setTitle(spu.getTitle());
        this.setSubTitle(spu.getSubTitle());
        this.setSaleable(spu.getSaleable());
        this.setValid(spu.getValid());
        this.setCreateTime(spu.getCreateTime());
        this.setLastUpdateTime(spu.getLastUpdateTime());
    }
    @Transient
    private SpuDetail spuDetail;
    @Transient
    private List<Sku> skus;
}