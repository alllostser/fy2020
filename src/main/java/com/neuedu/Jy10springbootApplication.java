package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.neuedu.dao")
@EnableScheduling //开启定时任务
public class Jy10springbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jy10springbootApplication.class, args);
    }

}
