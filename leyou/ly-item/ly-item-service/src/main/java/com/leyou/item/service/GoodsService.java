package com.leyou.item.service;

import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailsMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author javie
 * @date 2019/5/21 10:31
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailsMapper spuDetailsMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional
    public void addGoods(SpuBo spu) {
        //插入spu表
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(new Date());
        int insertSpu = spuMapper.insertSelective(spu);
        if (insertSpu==0){
            throw new LyException(ExceptionEnmu.INTERNAL_SAVE_ERROR);
        }
        //插入sku表
        List<Sku> skuList = spu.getSkus();
        if (skuList!=null&&skuList.size()>0){
            for (Sku sku:skuList){
                sku.setCreateTime(new Date());
                sku.setLastUpdateTime(new Date());
                sku.setSpuId(spu.getId());
                int i = skuMapper.insertSelective(sku);
                if(i==0){
                    throw new LyException(ExceptionEnmu.INTERNAL_SAVE_ERROR);
                }
                //插入stock表
                Stock stock = sku.getStock();
                if(stock!=null) {
                    stock.setSkuId(sku.getId());
                    int insertStock = stockMapper.insertSelective(stock);
                    if (insertStock==0){
                        throw new LyException(ExceptionEnmu.INTERNAL_SAVE_ERROR);
                    }
                }
            }
        }
        //插入SpuDetails表
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        int insertSku = spuDetailsMapper.insertSelective(spuDetail);
        if(insertSku==0){
            throw new LyException(ExceptionEnmu.INTERNAL_SAVE_ERROR);
        }

        // 发送 mq 消息
        amqpTemplate.convertAndSend("item.insert",spu.getId());

    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        return skuList;
    }

    public List<Sku> querySkuByIds(List<Long> ids){

        List<Sku> skuList = skuMapper.selectByIdList(ids);
        skuList.forEach(s->s.setStock(stockMapper.selectByPrimaryKey(s.getId())));
        return skuList;
    }
}
