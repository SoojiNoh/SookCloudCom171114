package com.smu.saason.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Created by mint on 11/04/2017.
 */

@Component
@PropertySource("classpath:/config.properties")
public class ConfigProperties {

    @Value("${fs.temp}")
    public String temp;

    @Value("${fs.location}")
    public String location;

    @Value("${fs.workspaceId}")
    public String workspaceId;

    @Value("${fs.s3.bucketName}")
    public String bucketName;

    @Value("${fs.s3.region}")
    public String region;

    public int BUFFER_SIZE = 1024*1024*4;

    // bufferedInputStream max size MB
    public int STREAM_BUFFER_SIZE = 1024*1024*200;
}

