package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService service;
    /**
     * 获取商品分类信息
     * */
    @RequestMapping(value = "/topcategory.do",method = RequestMethod.POST)
    public ServerResponse topCategory(Integer categoryId){
        ServerResponse sr = service.topCategory(categoryId);
        return sr;
    }

    /**
     * 获取商品详情
     * */
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ServerResponse detail(
            @RequestParam(value = "id",required = false,defaultValue = "0") Integer id,
            @RequestParam(value = "isNew",required = false,defaultValue = "0")Integer isNew ,
            @RequestParam(value = "isHot",required = false,defaultValue = "0")Integer isHot ,
            @RequestParam(value = "isBanner",required = false,defaultValue = "0")Integer isBanner
    ){
        ServerResponse sr = service.detail(id,isNew,isHot,isBanner);
        return sr;
    }

    /**
     * 商品搜索+动态排序
     * */


}


