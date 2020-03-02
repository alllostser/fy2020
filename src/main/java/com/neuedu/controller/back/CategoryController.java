package com.neuedu.controller.back;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.ICategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryController {
    @Resource
    ICategoryService service;

    /*       后台模块     */
    /**
     * 添加类别
     * */
    @RequestMapping(value = "/add_category.do",method = RequestMethod.GET)
    public ServerResponse addCategory(
            @RequestParam(value = "parentId",required =false,defaultValue = "0") Integer parentId,
            String categoryName,
            HttpSession session
    ){
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        if (user.getRole() != Consts.RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
        ServerResponse sr = service.addCategory(parentId,categoryName);
        return sr;
    }

    /**
     * 修改类别名称
     * @param categoryId
     * @param categoryName
     * @param session
     * @return
     */
    @RequestMapping(value = "/set_category_name.do",method = RequestMethod.GET)
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpSession session){
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        if (user.getRole() != Consts.RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
        ServerResponse sr = service.setCategoryName(categoryId,categoryName);
        return sr;
    }

    /**
     *获取品类子节点(平级)
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/get_category.do",method = RequestMethod.GET)
    public ServerResponse getCategory(Integer categoryId){
        ServerResponse sr = service.getCategory(categoryId);
        return sr;
    }
    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/get_deep_category.do",method = RequestMethod.GET)
    public ServerResponse getDeepCategory(Integer categoryId){
        ServerResponse sr = service.getDeepCategory(categoryId);
        return sr;
    }

    /*     前台模块   */
}
