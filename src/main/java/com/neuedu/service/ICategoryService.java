package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICategoryService {
    //后台-添加类别
    ServerResponse addCategory(Integer parentId, String categoryName);
    //后台-修改类别名称
    ServerResponse setCategoryName(Integer categoryId, String categoryName);
    //后台-获取品类子节点(平级)
    ServerResponse getCategory(Integer categoryId);
    //后台-获取当前分类id及递归子节点categoryId
    ServerResponse getDeepCategory(Integer categoryId);
}
