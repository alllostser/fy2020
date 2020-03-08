package com.neuedu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.CategoryDao;
import com.neuedu.dao.ProductDao;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.CategoryVo;
import com.neuedu.pojo.vo.ProductVo;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.PoToVoUtil;
import com.neuedu.utils.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {
    @Resource
    private ProductDao productDao;
    @Resource
    private CategoryDao categoryDao;
    @Resource
    private ICategoryService categoryService;
    /*后台模块*/

    /**
     * 新增OR更新产品
     *
     * @param product
     * @return
     */
    @Override
    public ServerResponse addOrUpdate(Product product) {
        //非空判断
        if (product == null) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(), StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Integer pid = product.getId();
        String subImages = product.getSubImages();//图片用逗号隔开1.png,2.png,3.png
        if (subImages != null && subImages.length() > 0) {
            String mainImage = subImages.split(",")[0];
            product.setMainImage(mainImage);
        }

        if (pid == null) {//添加商品
            int insertCount = productDao.insert(product);
            if (insertCount <= 0) {
                return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_INSTER_FAIL.getStatus(), Consts.ProductStatusEnum.PRODUCT_INSTER_FAIL.getDesc());
            } else {
                return ServerResponse.serverResponseBySucess("商品添加成功");
            }
        } else {//更新商品
            //step1:查询商品
            Product proCount = productDao.selectById(pid);
            if (proCount == null || proCount.getStatus() != 1) {  //更新的商品不存在
                return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getStatus(), Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getDesc());
            }
            //step2:更新商品
            int updateCount = productDao.updateProductBySelective(product);
            if (updateCount <= 0) {//更新失败
                return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_UPDATE_FAIL.getStatus(), Consts.ProductStatusEnum.PRODUCT_UPDATE_FAIL.getDesc());
            } else {//更新成功
                return ServerResponse.serverResponseBySucess("更新成功");
            }
        }
    }


    /* 前台模块*/

    /**
     * 获取商品分类信息
     */
    @Override
    public ServerResponse topCategory(Integer categoryId) {
        //非空判断
        if (categoryId == null || categoryId < 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(), StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //根据商品分类id查询所有一级子分类
        List<Category> categories = categoryDao.selectByParentId(categoryId);
        if (categories == null || categories.size() == 0) {
            return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.RETURN_IS_NULL.getStatus(), Consts.ProductStatusEnum.RETURN_IS_NULL.getDesc());
        }
        List<CategoryVo> vo = new ArrayList<>();
        //转换为vo对象
        for (Category c : categories) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(c.getId());
            categoryVo.setName(c.getName());
            categoryVo.setParentId(c.getParentId());
            categoryVo.setSortOrder(c.getSortOrder());
            categoryVo.setStatus(c.getStatus());
            categoryVo.setCreateTime(TimeUtils.dateToStr(c.getCreateTime()));
            categoryVo.setUpdateTime(TimeUtils.dateToStr(c.getUpdateTime()));
            vo.add(categoryVo);
        }
        return ServerResponse.serverResponseBySucess(vo);
    }

    /**
     * 获取商品详情
     */
    @Override
    public ServerResponse<ProductVo> detail(Integer id, Integer isNew, Integer isHot, Integer isBanner) {
        //非空判断
        if (id == null || id < 0) {
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(), StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Product pro = productDao.selectByIdForDetail(id, isNew, isHot, isBanner);
        if (pro == null) {
            return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_NOT_HAVE.getStatus(), Consts.ProductStatusEnum.PRODUCT_NOT_HAVE.getDesc());
        }
        //判断是否已经下架或被删除
        if (pro.getStatus() != 1){
           return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getStatus(), Consts.ProductStatusEnum.PRODUCT_RETURN_STATUS.getDesc());
        }
        ProductVo vo = null;
        try {
            vo = PoToVoUtil.productToProductVo(pro);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseBySucess(vo);
    }

    /**
     * 前台-商品搜索+动态排序
     */
    @Override
    public ServerResponse productList(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1:判读是否传递了categoryId和keyword
        if (categoryId == -1 && (keyword == null || "".equals(keyword))) {
            //前端没有传递categoryId和keyword,向前端返回空的数据
            //PageHelper
            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(productList);
            return ServerResponse.serverResponseBySucess(pageInfo);
        }
        List<Integer> categoryList = new ArrayList<>();
        //step2:判断CategoryId是否传递
        if (categoryId != -1) {//传递了CategoryId
            //查询categoryId下的所有子类
            ServerResponse<Set<Integer>> deepCategory = categoryService.getDeepCategory(categoryId);
            if (deepCategory.isSucess()) {
                Set<Integer> categoryIds = deepCategory.getData();
                Iterator<Integer> iterator = categoryIds.iterator();
                while (iterator.hasNext()) {
                    categoryList.add(iterator.next());
                }
            }
        }
        //step3:判断keyword是否传递
        if (keyword != null && !"".equals(keyword)) {
            keyword = "%" + keyword + "%";
        }
        //step4：执行查询
         //写在查询前
        PageHelper.startPage(pageNum,pageSize);
        if (orderBy != null && !"".equals(orderBy)){
            //filedname_desc/filedname_asc
            String[] orderbys = orderBy.split("_");
            PageHelper.orderBy(orderbys[0]+" "+orderbys[1]);
        }
        List<Product> products = productDao.findProductsByCategoryIdAndKeyword(categoryList, keyword);
         //转化vo对象
        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : products) {
            try {
                ProductVo vo = PoToVoUtil.productToProductVo(product);
                productVoList.add(vo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            //构建分页模型
        PageInfo pageInfo = new PageInfo(productVoList);
        //step5：返回结果

        return ServerResponse.serverResponseBySucess(pageInfo);
    }

    /**
     * 订单引用-商品扣库存
     * @param productId
     * @param quantity
     * @return
     */
    @Override
    public ServerResponse reduceStock(Integer productId, Integer quantity) {
        //非空判断
        if (productId == null || quantity == null){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //扣库存
        Product product = productDao.selectById(productId);
        if (product == null){
            return ServerResponse.serverResponseByFail(Consts.ProductStatusEnum.PRODUCT_NOT_HAVE.getStatus(),Consts.ProductStatusEnum.PRODUCT_NOT_HAVE.getDesc());
        }
        Integer stock = product.getStock();
        stock-=quantity;
        int count = productDao.reduceStock(productId,stock);
        if (count<=0){
            return ServerResponse.serverResponseByFail(Consts.OrderStatusEnum.REDUCESTOCK_FAILED.getStatus(),Consts.OrderStatusEnum.REDUCESTOCK_FAILED.getDesc());
        }
        return ServerResponse.serverResponseBySucess("减库存成功");
    }
}
