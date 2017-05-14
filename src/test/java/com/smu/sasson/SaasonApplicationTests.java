package com.smu.sasson;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	@Test
	public void contextLoads() {

	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SaasonApplicationTests.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SaasonApplicationTests.class, args);
	}

}
