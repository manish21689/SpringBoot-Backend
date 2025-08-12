package com.example.demo.service.callasync;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Async("taskExecutor")
    public void sendEmail() {
    	
        System.out.println("Sending email: " + Thread.currentThread().getName());
        int ctr=1;
        while(ctr<50) {
        System.out.println("email is sending to perrson"+ctr+"@yahoo.com");	
        ctr=ctr+1;	
        }
        
    }

    @Async("taskExecutor")
    public void writeSquaresToFile() throws IOException {
        System.out.println("Writing squares to file: " + Thread.currentThread().getName());
        BufferedWriter writer = new BufferedWriter(new FileWriter("squares.txt"));
        for (int i = 1; i <= 100000; i++) {
            writer.write(i + "^2 = " + (i * i));
            writer.newLine();
        }
        writer.close();
    }

    @Async("taskExecutor")
    public void exportDbDataToCsv() {
        System.out.println("Exporting DB data: " + Thread.currentThread().getName());
        for(int i=1;i<10;i++) {
        System.out.println("Fetch data and generate CSV logic "+i);	
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        	
        }
    }
    
    @Async("taskExecutor")
    public void sendEmailBatch(List<String> emailBatch) {
        for (String email : emailBatch) {
            // Simulate sending email (replace with real logic)
            System.out.println("Sending to: " + email + " by " + Thread.currentThread().getName());

            // Simulate delay
            try {
                Thread.sleep(5); // simulate sending time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    
}
