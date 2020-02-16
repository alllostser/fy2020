package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neuedu.dao")
public class Jy10springbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jy10springbootApplication.class, args);
    }

}
