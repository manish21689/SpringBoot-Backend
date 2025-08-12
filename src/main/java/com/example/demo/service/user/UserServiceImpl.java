package com.example.demo.service.user;

import com.example.demo.entity.Worker;

import com.example.demo.helper.MailService;

import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MailService emailService;

	@Override
	@Scheduled(cron = "0 0 17 ? * SAT") // Every Saturday 5 PM
	public void sendMsgAllWorker() {
		try {
			List<Worker> workers = userRepository.findAll();
			for (Worker worker : workers) {
			int retry=1;
			boolean isSent=false;
			while(isSent==false && retry<3)
				try {
					//emailService.sendEmail(worker.getEmail(), "Happy Saturday!","Dear " + worker.getName() + ",\n\nHave a great weekend!\n- Team");
					emailService.sendEmail("mkv.lko.up@gmail.com", "Happy Saturday!","Dear " + worker.getName() + ",\n\nHave a great weekend!\n- Team");
					isSent=true;
				} catch (Exception e) {
					retry++;
					
					 if (retry == 2) {
		                    System.out.println("Try to attemp 2 times for " +worker.getEmail() +" but all attempts failed");
		                }

				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}


//    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
//    @Override
//    @Scheduled(cron = "0 0 17 ? * SAT")
//    public void sendMsgAllWorker() {
//        List<Worker> workers = userRepository.findAll();
//        
//        for (Worker worker : workers) {
//            try {
//                emailService.sendEmail(worker.getEmail(), "Happy Saturday!",
//                        "Dear " + worker.getName() + ",\n\nHave a great weekend!\n- Team");
//                logger.info("Email sent to {}", worker.getEmail());
//            } catch (Exception e) {
//                logger.error("Error sending email to {}: {}", worker.getEmail(), e.getMessage(), e);
//            }
//        }
//    }

