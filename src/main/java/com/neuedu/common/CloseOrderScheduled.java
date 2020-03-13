package com.neuedu.common;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class CloseOrderScheduled {
    @Scheduled(cron = "0/2 * * * * ?")
    public void closeOrder(){
        System.out.println("======closeOrder====");
    }
}
