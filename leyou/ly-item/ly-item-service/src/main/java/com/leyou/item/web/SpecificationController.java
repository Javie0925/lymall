package com.leyou.item.web;

import com.leyou.item.pojo.Specification;
import com.leyou.item.service.SpecificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author javie
 * @date 2019/5/20 16:07
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;
    @GetMapping("{id}")
    public ResponseEntity<String> loadSpecificationBySid(@PathVariable Long id){

        Specification result = specificationService.queryById(id);
        if(result==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result.getSpecifications());
    }

    @PostMapping
    public ResponseEntity<String> addSpecifacation(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value="specifications") String specifications){

        if(StringUtils.isBlank(specifications)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        specificationService.addSpecifacation(categoryId,specifications);
        return ResponseEntity.ok().build();
    }
}
