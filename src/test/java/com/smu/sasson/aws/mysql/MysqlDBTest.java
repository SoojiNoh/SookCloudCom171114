package com.smu.sasson.aws.mysql;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
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

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	StudentDAO studentDAO;
	
	@Test
	public void testGetStudents() throws SQLException{
		List<Student> students = studentDAO.getStudents();
		students.stream().forEach(System.out::println);
	}

	@Test
	public void testInsertStudent() throws SQLException{
		Student student = new Student("mint1000",  "mintdf@gdf.com", "1111", System.currentTimeMillis());
		int insert = studentDAO.insert(student);
		Assert.assertEquals(1, insert);
		
	}
	
	@Test
	@Ignore
	public void testQuery() {
		List<TestRecord> result = jdbcTemplate.query("select username, email from student", (rs, rowNum) -> {
//			String id = rs.getString("id");
			String username = rs.getString("username");
			String email = rs.getString("email");
			return new TestRecord(username, email);
		});
		
	    result.stream().forEach(System.out::println);
	}
	

}

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
class TestRecord {
	
	@Getter private String username;
	@Getter private String name;

}

