package com.example.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//First Way to use
@Configuration
public class AsyncConfig {
 @Bean(name = "taskExecutor")
  Executor taskExecutor() {
     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
     executor.setCorePoolSize(3);
     executor.setMaxPoolSize(5);
     executor.setQueueCapacity(100);
     executor.setThreadNamePrefix("AsyncThread-");
     executor.initialize();
     return executor;
 }
}

//@Configuration
//@EnableAsync
// class AsyncConfig {
//
//    @Value("${async.corePoolSize}")
//    private int corePoolSize;
//
//    @Value("${async.maxPoolSize}")
//    private int maxPoolSize;
//
//    @Value("${async.queueCapacity}")
//    private int queueCapacity;
//
//    @Value("${async.threadNamePrefix}")
//    private String threadNamePrefix;
//
//    @Bean(name = "taskExecutor")
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(corePoolSize);
//        executor.setMaxPoolSize(maxPoolSize);
//        executor.setQueueCapacity(queueCapacity);
//        executor.setThreadNamePrefix(threadNamePrefix);
//        executor.initialize();
//        return executor;
//    }
//}

