package com.example.demo.service.callasync;



import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Post;

@Service
public class ApiAggregatorService {

    @Async
    public CompletableFuture<String> fetchDataFromApi1() {
        // Simulate API call
        return CompletableFuture.supplyAsync(() -> "API1: " + UUID.randomUUID());
    }

    @Async
    public CompletableFuture<String> fetchDataFromApi2() {
        return CompletableFuture.supplyAsync(() -> "API2: " + UUID.randomUUID());
    }

    public List<String> fetchAll() throws Exception {
        CompletableFuture<String> api1 = fetchDataFromApi1();
        CompletableFuture<String> api2 = fetchDataFromApi2();

        CompletableFuture.allOf(api1, api2).join();

        return List.of(api1.get(), api2.get());
    }
    
    @Scheduled(cron = "0 0 17 ? * SAT") // Every Saturday at 5 PM
    public void sendSaturdayGreeting() {
        System.out.println("Happy Saturday! Keep Smiling 😊");
        // You can replace this with DB save/email/send SMS
    }
    
    @Async
    @Cacheable(value = "posts", key = "#id")
    public CompletableFuture<Post> getPost(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/posts/" + id;
        Post post = restTemplate.getForObject(url, Post.class);
        return CompletableFuture.completedFuture(post);
    }

    @Transactional
    public void performTransactionalOperation() {
        // Dummy DB save logic here
        System.out.println("Inside transaction - all or nothing");
    }
}

