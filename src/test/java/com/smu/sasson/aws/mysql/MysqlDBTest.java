package com.smu.sasson.aws.mysql;

import java.sql.SQLException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.smu.saason.bean.Student;
import com.smu.saason.dao.StudentDAO;
import com.smu.sasson.SaasonApplicationTests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class MysqlDBTest extends SaasonApplicationTests {

//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	StudentDAO studentDAO;
	
	@Test
	public void testDB() throws SQLException{
		List<Student> students = studentDAO.getStudents();
		students.stream().forEach(System.out::println);
	}
	
//	@Test
//	@Ignore
//	public void testQuery() {
//		List<TestRecord> result = jdbcTemplate.query("select id, name from student", (rs, rowNum) -> {
//			String id = rs.getString("id");
//			String name = rs.getString("name");
//			return new TestRecord(id, name);
//		});
//		
//	    result.stream().forEach(System.out::println);
//	}
	

}

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
class TestRecord {
	
	@Getter private String id;
	@Getter private String name;

}

