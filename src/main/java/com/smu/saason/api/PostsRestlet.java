package com.smu.saason.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smu.saason.bean.Posts;
import com.smu.saason.dao.PostsDAO;

import net.minidev.json.parser.ParseException;

@RestController
@RequestMapping("/api")
public class PostsRestlet extends AbstractRestlet{
	@Autowired
	PostsDAO postsDAO;
	
    @RequestMapping(method = RequestMethod.GET, value = "/posts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String posts() throws ParseException, IOException, SQLException {
    	List<Posts> posts = postsDAO.getPosts();
    	return toJson(posts);
    }

	
}
