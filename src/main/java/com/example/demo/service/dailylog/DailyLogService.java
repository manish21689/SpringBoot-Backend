package com.example.demo.service.dailylog;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.DailyLogDTO;
import com.example.demo.entity.DailyLog;
import com.example.demo.entity.Worker;

public interface DailyLogService {
	//Common for both
	//Check At controller level ->both for admin and user no check
    DailyLogDTO createLog(DailyLogDTO log); 
    
  //Controller level only apna and Admin can update All
    DailyLogDTO updateLog(Long id, DailyLogDTO log);  
    
   
  
    //on Controller level // only user can get log only his team memebers and Admin can all 
    DailyLog getLogById(Long id);  
 // apne manager ke team main se kisi kab hi
    List<DailyLog> getLogsByUserId(Long userid);
    
     
  //user can get all data of his on the basis of manager but can get data any mangaer controller level
    Map<Long,List<DailyLog>> getLogsByManagerId(Long managerid);
  //use date per jitne log voh sab show lekin apne team ke but admin ho use date per sabke dekh sake
    List<DailyLog> getLogsByDate(LocalDate date); 
 // agar userid de jist team meber ka ho uska show use date ka aur agar userid nahi de raha hai toh use ka show ho
    List<DailyLog> getLogsByUserAndDate( Worker worker,LocalDate logdate); 

    //diffrent for both 
    //  only for User
    void deleteLog(Long id);
   //  only for User
    List<DailyLog> getLogs();
    
    //Only For admin 
    List<DailyLog> getAllLogs(); //For Admin Get All data no check
  //Only For admin
    void deleteAnyLog(Long id); // For Admin Delete Anyone check
  //Only For admin
    DailyLog getAnyLogById(Long id); //For Admin Any log from total data check

	
}


