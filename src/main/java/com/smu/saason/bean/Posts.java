package com.smu.saason.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
public class Posts {
	
	@Getter 
	private int id;
	
	@Getter 
	private String title;
	
	@Getter 
	private String url;
	
	@Getter
	private int keyword_id;
	
	@Getter
	private int provider_id;
	
	@Getter 
	private long created_at = System.currentTimeMillis();
	
//	@Getter
//	private String myfield;

}
