package com.neuedu.service.Impl;

import com.neuedu.common.ProductStatusEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.dao.CategoryDao;
import com.neuedu.dao.ProductDao;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.CategoryVo;
import com.neuedu.pojo.vo.ProductVo;
import com.neuedu.service.ProductService;
import com.neuedu.utils.PoToVoUtil;
import com.neuedu.utils.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;
    @Resource
    private CategoryDao categoryDao;
    /**
     * 获取商品分类信息
     * */
    @Override
    public ServerResponse topCategory(Integer categoryId) {
        //非空判断
        if (categoryId == null || categoryId<0){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        //根据商品分类id查询所有一级子分类
        List<Category> categories = categoryDao.selectByParentId(categoryId);
        if (categories == null || categories.size() == 0){
            return ServerResponse.serverResponseByFail(ProductStatusEnum.RETURN_IS_NULL.getStatus(),ProductStatusEnum.RETURN_IS_NULL.getDesc());
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
     * */
    @Override
    public ServerResponse detail(Integer id, Integer isNew, Integer isHot, Integer isBanner) {
        //非空判断
        if (id == null || id<0){
            return ServerResponse.serverResponseByFail(StatusEnum.PARAM_NOT_EMPTY.getStatus(),StatusEnum.PARAM_NOT_EMPTY.getDesc());
        }
        Product pro = productDao.selectByIdForDetail(id,isNew,isHot,isBanner);
        if (pro == null){
            return ServerResponse.serverResponseByFail(ProductStatusEnum.PRODUCT_RETURN_STATUS.getStatus(),ProductStatusEnum.PRODUCT_RETURN_STATUS.getDesc());
        }
        ProductVo vo = null;
        try {
            vo = PoToVoUtil.productToProductVo(pro);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseBySucess(vo);
    }
}
