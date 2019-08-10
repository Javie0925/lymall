package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SpuDetailsMapper;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuBo;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author javie
 * @date 2019/5/20 20:52
 */

@RestController
@RequestMapping("spu")
public class SpuController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuDetailsMapper spuDetailsMapper;

    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPageAndSort(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value="saleable",required = false) String saleable,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value="desc",defaultValue = "false") Boolean desc

    ){

        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+saleable);
        Boolean s = true;
        if("null".equals(saleable)){
            s = null;
        }else if ("true".equals(saleable)){
            s = true;
        }else if("false".equals(saleable)){
            s = false;
        }
        PageResult<SpuBo> pageResult = spuService.querySpuByPageAndSort(page,rows,s,key,desc);

        if(pageResult.getTotal()==0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(pageResult);
    }
    @GetMapping("detail/{id}")
    ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable Long id){
        SpuDetail spuDetail =  spuService.querySpuDetailById(id);
        if(spuDetail==null){
            return null;
        }
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){

        Spu spu = spuService.querySpuById(id);
        if (spu==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(spu);
    }
}
