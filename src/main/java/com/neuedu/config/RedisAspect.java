package com.neuedu.config;

import com.alibaba.fastjson.JSONObject;
import com.neuedu.common.RedisApi;
import com.neuedu.common.ServerResponse;
import com.neuedu.utils.MD5Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect  //声明切面类
@Component
public class RedisAspect {
    @Resource
    private RedisApi redisApi;
    //step1:定义切面点
    @Pointcut("execution(public * com.neuedu.service.Impl.CategoryServiceImpl.get*(..))")
    public void getPointcut(){}
    @Pointcut("execution(public * com.neuedu.service.Impl.CategoryServiceImpl.set*(..))")
    public void setPointcut(){}
    //step2:定义通知
    /**
     * 环绕通知
     * */
    @Around("getPointcut()")
    public Object aroundGet(ProceedingJoinPoint joinPoint){
        StringBuffer stringBuffer = new StringBuffer();
        //包名+类名
        String className = joinPoint.getSignature().getDeclaringTypeName();
        stringBuffer.append(className);
        //方法名
        String methodName = joinPoint.getSignature().getName();
        stringBuffer.append(methodName);
        Object[] args = joinPoint.getArgs();
        for (Object o : args) {//参数值数组
            stringBuffer.append(o);
        }
        //缓存key
        String cacheKey = MD5Utils.getMD5Code(stringBuffer.toString());
        //从缓存中读取数据
        String cacheValue = redisApi.get(cacheKey);
//        Gson gson =new Gson();
        if (cacheValue == null || "".equals(cacheValue)){//缓存数据为空
            //读DB
            try {
                //--------before-around--------


                Object proceed = joinPoint.proceed();//执行目标方法读取db
//                cacheValue =gson.toJson(proceed);
                cacheValue = JSONObject.toJSONString(proceed);
                //--------after-around-------
                //写入缓存
                String set = redisApi.set(cacheKey, cacheValue);
                return proceed;
            } catch (Throwable throwable) {
                System.out.println("--------afterthrowing-around-------");
                return throwable;

            }

        }else {

            ServerResponse serverResponse = JSONObject.parseObject(cacheValue,ServerResponse.class);
//            ServerResponse serverResponse = gson.fromJson(cacheValue, ServerResponse.class);
            return serverResponse;
        }

    }

    @Around("setPointcut()")
    public Object aroundSet(ProceedingJoinPoint joinPoint){
            try {
                //--------before-around--------


                Object proceed = joinPoint.proceed();//执行目标方法读取db
                //--------after-around-------
                //清空缓存
                String set = redisApi.flusdb();
                return proceed;
            } catch (Throwable throwable) {
                System.out.println("--------afterthrowing-around-------");
                return throwable;

            }

    }




















    /**
     * 前置通知
     * */
//    @Before("pointcut()")
//    public void before(){
//        System.out.println("====前置通知=====");
//    }
//
//    /**
//     * 后置通知
//     * */
//    @After("pointcut()")
//    public void after(){
//        System.out.println("====后置置通知=====");
//    }
//
//    /**
//     * 返回后通知
//     * */
//    @AfterReturning("pointcut()")
//    public void afterReturning(){
//        System.out.println("====返回后通知=====");
//    }
//
//    /**
//     * 抛出异常后通知
//     * */
//    @AfterThrowing("pointcut()")
//    public void beforeReturning(){
//        System.out.println("====抛出异常后通知=====");
//    }

}




