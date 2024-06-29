package com.example.memic.common.config.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {

    @Bean
    public Lock reentrantLock(){
        return new ReentrantLock(true);
    }
}
