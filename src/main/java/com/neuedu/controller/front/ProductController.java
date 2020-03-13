package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
/**
 *商品模块
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private IProductService service;
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
     * @param categoryId 类别id
     * @param keyword  关键字
     * @param pageNum 第几页
     * @param pageSize 一页多少条数据
     * @param orderBy 排序字段filedname_desc/filedname_asc
     * @return
     */
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    public ServerResponse list(
            @RequestParam(required = false,defaultValue = "-1")Integer categoryId,
            @RequestParam(required = false,defaultValue = "")String keyword,
            @RequestParam(value = "pageNum",required = false,defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(required = false,defaultValue = "")String orderBy
    ){
        ServerResponse sr = service.productList(categoryId,keyword,pageNum,pageSize,orderBy);
        return sr;
    }

}


