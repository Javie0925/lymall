package com.leyou.item.service;

import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author javie
 * @date 2019/5/20 16:13
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public Specification queryById(Long id) {

        Specification specification = new Specification();
        specification.setCategoryId(id);
        Specification selectOne = specificationMapper.selectByPrimaryKey(id);
        return selectOne;
    }

    public void addSpecifacation(Long categoryId, String specifications) {
        Specification insert = new Specification();
        insert.setCategoryId(categoryId);
        insert.setSpecifications(specifications);
        int i = specificationMapper.insert(insert);
        if(i==0) {
            throw new LyException(ExceptionEnmu.SPECIFICATIONS_INSERT_FAIL);
        }
    }
}
