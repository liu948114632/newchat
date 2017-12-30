package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Bean(name = "getExecutor")
    public Executor getExecutor(){
        return Executors.newFixedThreadPool(100);
    }
}
