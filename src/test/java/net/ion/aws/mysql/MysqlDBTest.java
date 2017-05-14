package net.ion.aws.mysql;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.test.sasson.SaasonApplicationTests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class MysqlDBTest extends SaasonApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Test
	public void testQuery() {
		List<TestRecord> result = jdbcTemplate.query("select id, name from test", (rs, rowNum) -> {
			String id = rs.getString("id");
			String name = rs.getString("name");
			return new TestRecord(id, name);
		});
		
		result.stream().forEach(System.out::println);
	}
	

}

@Accessors(fluent = true)
@AllArgsConstructor
@ToString
class TestRecord {
	
	@Getter private String id;
	@Getter private String name;

}

