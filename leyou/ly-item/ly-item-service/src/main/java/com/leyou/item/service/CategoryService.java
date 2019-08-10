package com.leyou.item.service;

import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryByPid(Long pid) {
        // 查询条件，mapper会把对象中的非空属性作为查询条件
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        // 判断查询结果
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnmu.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    public List<String> selectByIds(Long ...cids) {

        List<String> names = new ArrayList<>();
        for(Long id:cids){
            Category category = categoryMapper.selectByPrimaryKey(id);
            names.add(category.getName());
        }
        return names;
    }

    public List<Category> queryCategoryByIds(List<Long> ids) {

        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        return categoryList;
    }

    public List<String> queryNameByIds(List<Long> ids) {

        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        if (categoryList==null||categoryList.size()==0){
            return null;
        }
        List<String> nameList = new ArrayList<>();
        for (Category category : categoryList) {
            nameList.add(category.getName());
        }
        return nameList;
    }
}
