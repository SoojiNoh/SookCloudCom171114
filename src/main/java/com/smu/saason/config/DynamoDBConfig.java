package com.smu.saason.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class DynamoDBConfig {

//	@Bean
//	public AWSCredentials awsCredentials() {
//	  return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
//	}
//
//	@Bean 
//	public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials) {
//	  AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials);
//	  if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
//	    amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
//	  }
//	  return amazonDynamoDB;
//	}
//
//	@Bean
//	public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
//	  return new DynamoDB(amazonDynamoDB);
//	}

}
