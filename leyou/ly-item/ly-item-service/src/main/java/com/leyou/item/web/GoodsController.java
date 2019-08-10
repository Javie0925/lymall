package com.leyou.item.web;

import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/21 0:13
 */

@RestController
@RequestMapping
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @PostMapping("goods")
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spu){
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println(spu);
        if(spu == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            goodsService.addGoods(spu);
        }catch(LyException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("goods/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        List<Sku> skuList = goodsService.querySkuBySpuId(id);
        if (skuList==null||skuList.size()==0){
            return null;
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(skuList);
    }
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuByIds(@RequestParam("ids")List<Long> ids){

        return ResponseEntity.ok(goodsService.querySkuByIds(ids));
    }

}
