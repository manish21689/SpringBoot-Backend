package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.callasync.TaskService;

@RestController
public class TaskController {
    @Autowired TaskService taskService;
    
    //@Autowired private KafkaService kafkaService;

    @GetMapping("/start-tasks")
    public String startTasks() {
        taskService.sendEmail();
        try {
			taskService.writeSquaresToFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        taskService.exportDbDataToCsv();
        return "Tasks started asynchronously!";
    }
    
    @GetMapping("/send-bulk-emails")
    public String sendBulkEmails() {
        // Simulate 10,000 emails
        List<String> allEmails = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            allEmails.add("user" + i + "@example.com");
        }

        // Split into batches of 1000
        int batchSize = 1000;
        for (int i = 0; i < allEmails.size(); i = i + batchSize) {
            int end = Math.min(i + batchSize, allEmails.size());
            List<String> batch = allEmails.subList(i, end);
            taskService.sendEmailBatch(batch); // async call
            
        }

        return "Email sending started!";
    }
//🔹 Approach 2: Using ExecutorService Manually
    
    @GetMapping("/sendEmailsExecutor")
    public String sendEmailsWithExecutor() {
        List<String> allEmails = getDummyEmailList();

        int batchSize = 1000;
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < allEmails.size(); i += batchSize) {
            int start = i;
            int end = Math.min(i + batchSize, allEmails.size());

            List<String> batch = new ArrayList<>(allEmails.subList(start, end));

            executor.submit(() -> {
                for (String email : batch) {
                    System.out.println(Thread.currentThread().getName() + " sending to: " + email);
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                }
            });
        }

        executor.shutdown();

        return "Emails sending using ExecutorService started!";
    }

    private List<String> getDummyEmailList() {
        List<String> emails = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            emails.add("user" + i + "@mail.com");
        }
        return emails;
    }
    
   

//    @PostMapping("/kafka/send")
//    public ResponseEntity<String> send(@RequestParam String message) {
//        kafkaService.sendMessage("myTopic", message);
//        return ResponseEntity.ok("Sent");
//    }
//
//    @KafkaListener(topics = "myTopic", groupId = "myGroup")
//    public void listen(String message) {
//        System.out.println("Received: " + message);
//    }
    
}

//List<String> allEmails = Arrays.asList("a@gmail.com", "b@gmail.com", "c@gmail.com", "d@gmail.com");
//List<String> batch = allEmails.subList(0, 2);
//System.out.println(batch); // Output: [a@gmail.com, b@gmail.com]

//List<String> batch = allEmails.subList(i, end); 
//or
//List<String> batch = new ArrayList<>();
//for (int j = i; j < end; j++) {
//    batch.add(allEmails.get(j));
//}