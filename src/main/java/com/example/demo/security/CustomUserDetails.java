package com.example.demo.security;



import com.example.demo.entity.Worker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Worker worker;

    public CustomUserDetails(Worker worker) {
        this.worker = worker;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(worker.getRole()));
    }

    @Override
    public String getPassword() {
        return worker.getPassword();  // woker will return password
    }

    @Override
    public String getUsername() {
        return worker.getEmail(); // use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // customize as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // customize as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // customize as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // customize as needed
    }

    public Worker getWorker() {
        return worker; // to get full Worker object when needed
    }
}

