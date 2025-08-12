//package com.example.demo.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
//public class ConnFromApplcationProp {
//	private String url;
//	private String username;
//	private String password;
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	@Bean
//	public DataSource dataSource() {
//		DriverManagerDataSource ds = new DriverManagerDataSource();
//		ds.setUrl(url);
//		ds.setUsername(username);
//		ds.setPassword(password);
//		return ds;
//	}
//
//}
//if configuration file is given then it will be used but values should be present in applcation.prop too 
