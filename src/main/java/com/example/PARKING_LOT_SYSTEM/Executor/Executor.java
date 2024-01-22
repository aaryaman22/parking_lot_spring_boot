package com.example.PARKING_LOT_SYSTEM.Executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
public class Executor {
    @Scheduled(cron = "0 0/1 * * * ?")
    public void forceUnpark() // yet to be implmented
    {
        System.out.println("unparking Vehicle");
        log.info("Unpark Vehicle");
    }
}
