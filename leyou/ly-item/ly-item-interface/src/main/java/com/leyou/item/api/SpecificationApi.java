package com.leyou.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author javie
 * @date 2019/5/20 16:07
 */
@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("{id}")
    public ResponseEntity<String> loadSpecificationBySid(@PathVariable("id") Long id);

}
