package com.smu.saason.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
public class Student {
	
	@Getter 
	private String username;
	
	@Getter 
	private String email;
	
	@Getter 
	private String password;
	
	@Getter 
	private long create_time = System.currentTimeMillis();

}
