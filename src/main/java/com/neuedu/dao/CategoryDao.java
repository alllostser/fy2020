package com.neuedu.dao;

import com.neuedu.pojo.Category;

import java.util.List;

public interface CategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
    //根据商品分类id查询所有一级子分类
    List<Category> selectByParentId(Integer id);
}