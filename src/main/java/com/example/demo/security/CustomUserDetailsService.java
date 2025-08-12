package com.example.demo.security;



import com.example.demo.dto.LoggedInUserDTO;
import com.example.demo.entity.Worker;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository workerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        LoggedInUserDTO loggedInUser = new LoggedInUserDTO(
        	    worker.getId(),
        	    worker.getEmail(),
        	    worker.getRole(),
        	    worker.getPassword(),             // <- password (String)
        	    worker.getManager().getId()       // <- managerid (Long)
        	);
        return loggedInUser;
       //return new CustomUserDetails(worker);
    }
}



// 1 create custom class implements user details joh inbuid rakhta hai getusernanme() and getpassword() isaccountexpired like this
// 2 aap eske construture apne worker class /user class/entity pass kar dijiye
// 3 aur worker ke entity impor kar lijiye
// 4 jahan per getusernamename return ho raha worker.getEmail ya worker.getName kar sakte hai
 // same for password
// 5 customuserdetailsservice name ek service banayae and use implements userdetailsservie kar dijiye esme loaduserbyusernane hota jiske ander worker.repofindbyemail()
// 6 retun kar new object of customeruserdetails jo aur uske constructoer main worker.repofindbyemail(email ) pass kar dijiye 
// 7 pass CustomUserDetailsService in SecurityConfig constructrur alog with jwtAtuthentication filter