package com.neuedu.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect  //声明切面类
@Component
public class RedisAspect {
    //step1:定义切面点
    @Pointcut("execution(public * com.neuedu.service.Impl.ProductServiceImpl.detail(..))")
    public void test(){

    }

    //step2:定义通知

    /**
     * 前置通知
     * */
//    @Before("test()")
//    public void before(){
//        System.out.println("====前置通知=====");
//    }
//
//    /**
//     * 后置通知
//     * */
//    @After("test()")
//    public void after(){
//        System.out.println("====后置置通知=====");
//    }
//
//    /**
//     * 返回后通知
//     * */
//    @AfterReturning("test()")
//    public void afterReturning(){
//        System.out.println("====返回后通知=====");
//    }
//
//    /**
//     * 抛出异常后通知
//     * */
//    @AfterThrowing("test()")
//    public void beforeReturning(){
//        System.out.println("====抛出异常后通知=====");
//    }

    /**
     * 环绕通知
     * */
    @Around("test()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object proceed=null;
        try {
            System.out.println("--------before-around--------");
            proceed = joinPoint.proceed();//执行目标方法
            System.out.println("--------after-around-------");
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("--------afterthrowing-around-------");
        }
        System.out.println("-------afterReturning--------");
        return proceed;
    }



}
