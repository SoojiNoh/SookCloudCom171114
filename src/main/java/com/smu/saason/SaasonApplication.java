package com.smu.saason;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.smu.saason.api.NaverITNewsCrawler;
import com.smu.saason.api.NaverITNewsCrawler2;

@SpringBootApplication
public class SaasonApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SaasonApplication.class);
    }

    public static void main(String[] args) throws ClientProtocolException, IOException {
        SpringApplication.run(SaasonApplication.class, args);
        NaverITNewsCrawler.main(args);
        // NaverITNewsCrawler2.main(args);
    }
}