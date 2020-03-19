package com.neuedu.controller;

import com.neuedu.common.RedisApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import javax.annotation.Resource;

@RestController
public class JedisDemo1{

    @Resource
    RedisApi redisApi;

    /**
     * 连接池连接方式
     */
    @RequestMapping("/redis.do")
   public Long get(){
        Long name = redisApi.del("name");
        return name;
   }

    /**
     * 不使用连接池连接方式
     */
    public static void main(String[] args){
        // 1. 设置IP地址和端口
        Jedis jedis = new Jedis("123.56.75.159",6379);
//        Jedis jedis1 = new Jedis("123.57.49.162",6381);
        // 2. 保存数据
        jedis.set("name","imooc");
        // 3. 获取数据
        String value = jedis.get("name");
        System.out.println(value);
        // 4.释放资源
        jedis.close();


    }

}