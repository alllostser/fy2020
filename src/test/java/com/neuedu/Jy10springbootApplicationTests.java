package com.neuedu;

import com.neuedu.common.RedisApi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Jy10springbootApplicationTests {
    @Resource
    private RedisApi redisApi;
    @Test
    void contextLoads() {
        String s = redisApi.get("123");
        System.out.println(s);
    }

}
