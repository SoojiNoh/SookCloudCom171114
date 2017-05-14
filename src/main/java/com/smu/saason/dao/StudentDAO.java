package com.smu.saason.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.smu.saason.bean.Student;

@Component
public class StudentDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Student> getStudents() throws SQLException {
		List<Student> result = jdbcTemplate.query("select id, name from student", (rs, rowNum) -> {
			String id = rs.getString("id");
			String name = rs.getString("name");
			return new Student(id, name);
		});
		
		return result;
	}
}
