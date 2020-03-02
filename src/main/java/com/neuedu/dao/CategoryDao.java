package com.neuedu.dao;

import com.neuedu.pojo.Category;

import java.util.List;

public interface CategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Category record);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
    //查询该类别
    Category selectByPrimaryKey(Integer id);
    // 添加类别
    int insert(Category record);
    //判断是否已有相同类别
    List<String> selectNameByParentId(Integer parentId);
    //根据商品分类id查询所有一级子分类
    List<Category> selectByParentId(Integer id);
    //获取品类子节点(平级)
    List<Category> getSubCategorysById(Integer categoryId);
}