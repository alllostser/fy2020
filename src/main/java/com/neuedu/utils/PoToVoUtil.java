package com.neuedu.utils;

import com.neuedu.pojo.Product;
import com.neuedu.pojo.vo.ProductVo;

import java.io.IOException;

public class PoToVoUtil {
    /**
     * 商品转商品vo
     * @param product
     * @return
     * @throws IOException
     */
    public static ProductVo productToProductVo(Product product) throws IOException {
        ProductVo vo = new ProductVo();
        vo.setImageHost(PropertiesUtil.getProperty("imageHost"));
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setSubtitle(product.getSubtitle());
        vo.setMainImage(product.getMainImage());
        vo.setSubImages(product.getSubImages());
        vo.setDetail(product.getDetail());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCreateTime(TimeUtils.dateToStr(product.getCreateTime()));
        vo.setUpdateTime(TimeUtils.dateToStr(product.getUpdateTime()));
        return vo;
    }

}
