package com.neuedu.controller.back;

import com.neuedu.common.Consts;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import com.neuedu.utils.PropertiesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/manage/product")
public class BProductController {
    @Resource
    private IProductService service;
    /**
     * 图片上传
     * */
//    @RequestMapping(value = "/upload.do",method = RequestMethod.GET)
//    public String upload(){
//        return "upload";
//    }
    @RequestMapping(value = "/do_upload.do",method = RequestMethod.POST)
    public ServerResponse doUpload(@RequestParam("pic") MultipartFile file){
        if (file.isEmpty()){
            return ServerResponse.serverResponseByFail(500,"上传文件不能为空");}
        //step1:获取文件的名称
        String filename = file.getOriginalFilename();
        //step2:获取原文件的扩展名
        String ext = filename.substring(filename.lastIndexOf("."));
        //step3:定义新的文件名,为文件生产一个唯一名称
        String newName = UUID.randomUUID().toString();
        String newFilename = newName+System.currentTimeMillis()+ext;
        try {
            //step5:创建文件
            File newFile = new File(PropertiesUtil.getProperty("imageHost")+PropertiesUtil.getProperty("upload"),newFilename);
            newFile.mkdirs();
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseBySucess("上传成功");
    }

    /**
     * 新增OR更新产品
     * @return
     */
    @RequestMapping(value = "/save.do",method = RequestMethod.GET)
    public ServerResponse save(Product product, HttpSession session){
        //step1:判断用户是否已经登录
        User user = (User) session.getAttribute(StatusEnum.LOGIN_USER);
        if (user == null){//未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        //step2:判断权限
        if (user.getRole()!= Consts.RoleEnum.ADMIN.getRole()){
            return ServerResponse.serverResponseByFail(StatusEnum.USER_LIMITED_AUTHORITY.getStatus(),StatusEnum.USER_LIMITED_AUTHORITY.getDesc());
        }
        ServerResponse sr = service.addOrUpdate(product);
        return sr;
    }

}
