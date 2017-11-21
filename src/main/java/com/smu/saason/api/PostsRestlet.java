package com.smu.saason.api;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smu.saason.bean.Posts;
import com.smu.saason.bean.Student;
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
    
    @RequestMapping(method = RequestMethod.POST, value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createNewPost(
    		@PathVariable(required=true) int id,
    		@RequestParam(required=true) String title,
    		@RequestParam(required=true) String url,
    		@RequestParam(required=true) int keyword_id,
    		@RequestParam(required=true) int provider_id
    		)
    throws ParseException, IOException, SQLException {
    	SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		String now = date.format(new Date());
    	postsDAO.insert(new Posts(id, title, url, keyword_id, provider_id, now, now));
    }
}
