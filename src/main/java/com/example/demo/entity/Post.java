package com.example.demo.entity;

public class Post {
    private Long id;
    private String title;
    private String body;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Post(String title, String body) {
		super();
		this.title = title;
		this.body = body;
	}
    
}
