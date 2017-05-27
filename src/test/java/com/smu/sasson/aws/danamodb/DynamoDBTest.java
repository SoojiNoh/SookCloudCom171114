package com.smu.sasson.aws.danamodb;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.tomcatsessionmanager.amazonaws.services.dynamodbv2.util.Tables;
import com.smu.sasson.SaasonApplicationTests;

public class DynamoDBTest extends SaasonApplicationTests {
	private static String TBL_NAME = "mint_test2";
	
	static DynamoDB ddb = null;
    @BeforeClass
    public static void loadSystemLib() throws NoSuchFieldException, IllegalAccessException {
		ClasspathPropertiesFileCredentialsProvider classpathPropertiesFileCredentialsProvider = new ClasspathPropertiesFileCredentialsProvider();

		ddb = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2)
				.withCredentials(classpathPropertiesFileCredentialsProvider).build());

    }
    
    
	@Test
	public void t1CreateTable() throws InterruptedException {
		Table table = ddb.getTable(TBL_NAME+"12");
		
		System.out.println(table.getTableName());
		TableDescription description = table.getDescription();

		if (description == null) {
			CreateTableRequest ctr = new CreateTableRequest().withTableName(TBL_NAME)
					.withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
					.withAttributeDefinitions(new AttributeDefinition("f1", ScalarAttributeType.S))
					.withKeySchema(new KeySchemaElement("f1", KeyType.HASH));

			table = ddb.createTable(ctr);
			table.waitForActive();

			System.out.println("Table created : " + TBL_NAME);
		}
	}

	@Test
	public void t2PutItem() throws InterruptedException {
		Table table = ddb.getTable(TBL_NAME);
		
		Item item = new Item();
		item.with("f1", "my f1 value");
		item.with("f2", "my f2 value");
		item.with("f3", "my f3 value");
		
		table.putItem(item);
	}

}
