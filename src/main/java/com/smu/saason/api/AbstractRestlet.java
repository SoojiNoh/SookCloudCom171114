package com.smu.saason.api;

import java.util.List;

import com.google.gson.Gson;

public class AbstractRestlet {

	protected <T> String toJsonstring(List<T> list){
    	Gson gson = new Gson();
        return gson.toJson(list);
	}
}
