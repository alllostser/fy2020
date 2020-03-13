package com.neuedu.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.neuedu.common.ServerResponse;
import com.neuedu.common.StatusEnum;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.PropertiesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private IOrderService service;
    /*  前台模块   */


    /*
     *创建订单
     * */
    @RequestMapping(value = "/create.do",method = RequestMethod.GET)
    public ServerResponse create(Integer shippingId, HttpSession session){
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //step1:判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        ServerResponse sr = service.createOrder(user.getId(),shippingId);
        return sr;
    }

    /**
     *取消订单
     * @param orderNo
     * @param session
     * @return
     */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(Long orderNo,HttpSession session){
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //step1:判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        Integer userId = user.getId();
        ServerResponse serverResponse = service.cancelOrder(orderNo);
        return serverResponse;
    }

    /**
     *获取订单列表（用户）
     * @param pageSize
     * @param pageNum
     * @param session
     * @return
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(
            @RequestParam(value = "pageSize",required = false,defaultValue = "0") Integer pageSize,
            @RequestParam(value = "pageNum",required = false,defaultValue = "10")Integer pageNum,
            HttpSession session)
            {
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //step1:判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        Integer userId = user.getId();
        ServerResponse serverResponse = service.orderList(userId,pageNum,pageSize);
        return serverResponse;
    }

    /**
     *用户支付
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/pay.do")
    public ServerResponse list(Long orderNo,HttpSession session)
    {
        User user =(User) session.getAttribute(StatusEnum.LOGIN_USER);
        //step1:判断用户是否登录
        if (user == null){
            //未登录
            return ServerResponse.serverResponseByFail(StatusEnum.USER_NOT_LOGIN.getStatus(),StatusEnum.USER_NOT_LOGIN.getDesc());
        }
        Integer userId = user.getId();
        ServerResponse serverResponse = service.orderPay(userId,orderNo);
        return serverResponse;
    }

    /**
     * 支付宝回调接口
     * @param request
     * @return
     */
    @RequestMapping("/alipay_callback.do")
    public String alipayCallback(HttpServletRequest request){
        System.out.println("支付回调接口");

        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
        Map<String,String> signMap = new HashMap<>();
        while (iterator.hasNext()){
            Map.Entry<String, String[]> entry = iterator.next();
            String key = entry.getKey();
            String[] values = entry.getValue();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i <values.length ; i++) {
                buffer.append(values[i]+",");
            }
            String value = buffer.toString();
            value = value.substring(0, value.length()-1);
            signMap.put(key,value);
            System.out.println("key="+key+"    value="+value);

        }
        //1.验证签名
        signMap.remove("sign_type");
        try {
            boolean result = AlipaySignature.rsaCheckV2(signMap,PropertiesUtil.getProperty("alipay_public_key"), "utf-8", "RSA2");
            if (!result){
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //2.处理业务逻辑
        String callback = service.alipayCallback(signMap);
        return callback;
    }



}
