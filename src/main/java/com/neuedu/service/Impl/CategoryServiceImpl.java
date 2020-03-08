package com.neuedu.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.neuedu.common.Consts;
import com.neuedu.common.RedisApi;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryDao;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Resource
    private CategoryDao dao;
    @Resource
    private RedisApi redisApi;
    /* 后台模块*/

    /**
     * 添加类别
     * @param parentId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse addCategory(Integer parentId, String categoryName) {
        //step1：参数非空判断
            //类别名非空判断
        if ( categoryName==null || "".equals(categoryName)){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_NAME_NULL.getStatus(), Consts.CategoryStatusEnum.CATEGORY_NAME_NULL.getDesc());
        }
        //类别id非空判断
        if (parentId == null || parentId<0 ){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getDesc());
        }
        //step2:判断是否已有相同类别
        List<String> name = dao.selectNameByParentId(parentId);
        for (String s : name) {
            System.out.println(s);
            if (s.equals(categoryName)){
                return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ALREADY_EXIST.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ALREADY_EXIST.getDesc());
            }
        }

        //step3：生成类别实体类
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        //step4:插入到数据库
        int result = dao.insert(category);
        if (result<=0){
            //插入失败
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_INSTER_FAIL.getStatus(),Consts.CategoryStatusEnum.CATEGORY_INSTER_FAIL.getDesc());
        }
        return ServerResponse.serverResponseBySucess("添加成功");
    }

    /**
     * 修改品类名字
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        //step1：参数非空判断
            //类别名非空判断
        if ( categoryName==null || "".equals(categoryName)){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_NAME_NULL.getStatus(), Consts.CategoryStatusEnum.CATEGORY_NAME_NULL.getDesc());
        }
            //类别id非空判断
        if (categoryId == null || categoryId<0 ){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getDesc());
        }
        //step2:查询该类别是否存在
        Category category = dao.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_INEXISTENCE.getStatus(),Consts.CategoryStatusEnum.CATEGORY_INEXISTENCE.getDesc());
        }
        //step3:判断是否已有相同类别
        List<String> name = dao.selectNameByParentId(category.getParentId());
        for (String s : name) {
            System.out.println(s);
            if (s.equals(categoryName)){
                return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ALREADY_EXIST.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ALREADY_EXIST.getDesc());
            }
        }
        //step3：修改类别名称
        category.setName(categoryName);
        int result = dao.updateByPrimaryKey(category);
        if (result<=0){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.ERROR.getStatus(),Consts.CategoryStatusEnum.ERROR.getDesc());
        }
        return ServerResponse.serverResponseBySucess("修改成功");
    }

    /**
     *获取品类子节点(平级)
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse getCategory(Integer categoryId) {
        //step1:非空判断
            //类别id非空判断
        if (categoryId == null || categoryId<0 ){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getDesc());
        }
//        String key = "categoryId_"+categoryId;
//        String s = redisApi.get(key);
//        if (s != null && !"".equals(s)){//如果不为空
//            //将redis中的json数据转换为集合返回
//            return ServerResponse.serverResponseBySucess(JSONObject.parseArray(s, Category.class));
//        }
        //step2:获取该分类下子分类
        List<Category> categorys = dao.getSubCategorysById(categoryId);
        //step3：判断该分类是否有子分类
        if (categorys.size() <= 0){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.NO_CHILDREN_CATEGORY.getStatus(),Consts.CategoryStatusEnum.NO_CHILDREN_CATEGORY.getDesc());
        }
        //将集合转换为json添加到reids中
//        String set = redisApi.set(key, JSONObject.toJSONString(categorys));
        return ServerResponse.serverResponseBySucess(categorys);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse getDeepCategory(Integer categoryId) {
        //step1:非空判断
            //类别id非空判断
        if (categoryId == null || categoryId<0 ){
            return ServerResponse.serverResponseByFail(Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getStatus(),Consts.CategoryStatusEnum.CATEGORY_ID_NULL.getDesc());
        }
//        String key = "categoryList_"+categoryId;
//        String s = redisApi.get(key);
//        if (s !=null && !"".equals(s)){//如果不为空
//
//        }
        Set<Integer> set = new HashSet<>();
        Set<Integer> result = findAllSubCategory(set, categoryId);
        return ServerResponse.serverResponseBySucess(result);
    }

    /**
     * 获取当前分类id及递归子节点的递归方法
     * @param categoryIds
     * @param categoryId
     * @return
     */
    private Set<Integer> findAllSubCategory(Set<Integer> categoryIds,Integer categoryId){
        //step1:先根据categoryId查询类别
        Category category = dao.selectByPrimaryKey(categoryId);
        if (category != null){
            categoryIds.add(category.getId());
        }
        //step2:查询category下所有的一级子节点
        ServerResponse<List<Category>> serverResponse = getCategory(categoryId);
        if (!serverResponse.isSucess()){
            return categoryIds;
        }
        List<Category> categoryList = serverResponse.getData();
        for (Category c : categoryList) {
            findAllSubCategory(categoryIds, c.getId());
        }
        return categoryIds;
    }

    /*      前台模块   */
}
