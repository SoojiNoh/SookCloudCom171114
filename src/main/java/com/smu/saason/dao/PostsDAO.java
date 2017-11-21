package com.smu.saason.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.smu.saason.bean.Posts;

@Component
public class PostsDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Posts> getPosts() throws SQLException {
		List<Posts> result = jdbcTemplate.query("select * from posts", (rs, rowNum) -> {
			return new Posts(rs.getInt("id"), rs.getString("title"), rs.getString("url"), rs.getInt("keyword_id"), rs.getInt("provider_id"), rs.getString("created_at"), rs.getString("updated_at"));
		});
		
		return result;
	}

	public int insert(Posts post) throws SQLException {
		return jdbcTemplate.update("insert into posts values (?,?,?,?,?,?,?)", post.id(), post.title(), post.url(), post.keyword_id(), post.provider_id(), post.created_at(), post.updated_at());
	}
}
