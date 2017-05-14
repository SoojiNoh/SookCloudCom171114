package com.smu.sasson.aws.danamodb;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class DynamoDBTest {

	public static void main(String[] args) throws InterruptedException {
		String tblName = "mint_test2";
		
		ClasspathPropertiesFileCredentialsProvider classpathPropertiesFileCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		
		DynamoDB ddb = new DynamoDB(AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.AP_NORTHEAST_2)
			.withCredentials(classpathPropertiesFileCredentialsProvider)
			.build()); 
		
		
		Table table = ddb.getTable(tblName);
		
		TableDescription description = table.getDescription();
		
		if (description == null){
			CreateTableRequest ctr = new CreateTableRequest()
			.withTableName(tblName)
			.withProvisionedThroughput(new ProvisionedThroughput(100L, 20L))
			.withAttributeDefinitions(
				new AttributeDefinition("f1", ScalarAttributeType.S))
			.withKeySchema(
				new KeySchemaElement("f1", KeyType.HASH));
		
			
			table = ddb.createTable(ctr);
			table.waitForActive();
			
			System.out.println("Table created : " + tblName);
			
		}

		
		
		Item item = new Item()
			.with("f1", "aaaa")
			.with("f2", 1212L);
		
		table.putItem(item);
		
		Item item2 = table.getItem("f1", "aaaa");
		System.out.println(item2.get("f1").toString());
		
		
	}
	
}
