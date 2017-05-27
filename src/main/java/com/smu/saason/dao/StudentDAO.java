package com.smu.saason.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
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
		List<Student> result = jdbcTemplate.query("select * from student", (rs, rowNum) -> {
			return new Student(rs.getString("username"), rs.getString("email"),rs.getString("password"), rs.getTimestamp("create_time").getTime());
		});
		
		return result;
	}

	public int insert(Student student) throws SQLException {
		return jdbcTemplate.update("insert into student values (?,?,?,?)", student.username(), student.email(), student.password(), new Timestamp(student.create_time()));
	}
}
