package com.smu.sasson;
import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.smu.saason.SaasonApplication;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=SaasonApplication.class)
public class SaasonApplicationTests {
	@Autowired
	private DataSource ds;
	
	@Test
	public void contextLoads() {

	}
	
	@Test
	public void testConnection() throws Exception {
		System.out.println("ds: " + ds);
		
		Connection con = ds.getConnection();
		
		System.out.println("con: " + con);
		
		con.close();
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SaasonApplicationTests.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SaasonApplicationTests.class, args);
	}

}
