package com.test.saason.repositories;


import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.datasource")
public class DBConfiguration {
	
	@Value("${spring.datasource.username}")
    private String username;

	@Value("${spring.datasource.password}")
    private String password;

	@Value("${spring.datasource.url}")
    private String url;
    
	@Value("${spring.datasource.drvier-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClass;

    @Value("#{T(java.lang.Integer).parseInt('${spring.datasource.initialConnectionSize}')}")
    private int initialConnectionSize;

    @Value("#{T(java.lang.Integer).parseInt('${spring.datasource.maxPoolSize}')}")
    private int maxPoolSize;
    

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    DataSource dataSource() throws SQLException {
        DataSource dataSource = new DataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setInitialSize(initialConnectionSize);
        dataSource.setMaxActive(maxPoolSize);
        return dataSource;
    }
    
}

