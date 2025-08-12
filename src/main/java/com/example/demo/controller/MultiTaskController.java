package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Post;
import com.example.demo.service.callasync.ApiAggregatorService;

@RestController
@RequestMapping("/multi")
public class MultiTaskController {

    @Autowired
    private ApiAggregatorService apiAggregatorService;

    @GetMapping("/fetch-all")
    public ResponseEntity<List<String>> fetchAll() throws Exception {
        return ResponseEntity.ok(apiAggregatorService.fetchAll());
    }
    
    @GetMapping("/fetch-posts")
    public ResponseEntity<List<Post>> fetchMultiplePosts() throws Exception {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<CompletableFuture<Post>> futures = new ArrayList<>();

        for (Long id : ids) {
            futures.add(apiAggregatorService.getPost(id));
        }

        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

//     // Step 1: Convert list to array
//        CompletableFuture<?>[] futureArray = futures.toArray(new CompletableFuture[0]);
//
//        // Step 2: Create one combined future that completes when all are done
//        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureArray);
//
//        // Step 3: Wait for all to finish
//        allFutures.join();
        
        List<Post> posts = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        return ResponseEntity.ok(posts);
    }

}

