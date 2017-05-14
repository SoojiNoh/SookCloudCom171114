package com.smu.saason.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
public class Student {
	
	@Getter private String id;
	@Getter private String name;

}
