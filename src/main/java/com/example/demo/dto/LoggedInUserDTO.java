package com.example.demo.dto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//BECAUSE YOU IMPLEMENTED THIS THAN IT WILL STORE SECURITY CONTEXT HOLDER AND LOADUSERBYANME
//ELSE Generally UserDetails is used in security context and loaduserbyname
public class LoggedInUserDTO implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String email;
    private String role;
    private String password;
    private Long managerid;

    // Constructor
    public LoggedInUserDTO(Long id, String email, String role, String password, Long managerid) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.password = password;
        this.managerid = managerid;
    }

    public LoggedInUserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getManagerid() { return managerid; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setPassword(String password) { this.password = password; }
    public void setManagerid(Long managerid) { this.managerid = managerid; }

    // Spring Security methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
