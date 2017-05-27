package com.smu.saason.api;

import com.google.gson.Gson;

public class AbstractRestlet {

	protected <T> String toJson(T obj){
    	Gson gson = new Gson();
        return gson.toJson(obj);
	}
}
