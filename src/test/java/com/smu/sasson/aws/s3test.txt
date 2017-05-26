package com.smu.sasson.aws.s3;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.smu.saason.config.ConfigProperties;
import com.smu.saason.repository.s3.BinaryData;
import com.smu.saason.repository.s3.RepositoryException;
import com.smu.saason.repository.s3.S3Client;
import com.smu.sasson.SaasonApplicationTests;

/**
 * Created by mint on 13/04/2017.
 */
public class BinaryDataTest extends SaasonApplicationTests{

    @Autowired
    private ConfigProperties configProperty;


    @Test
    public void testPutBinary() throws FileNotFoundException, RepositoryException {
        InputStream is = this.getClass().getResourceAsStream("/test/test1.jpg");
        BinaryData binaryData = new BinaryData(is);
        System.out.println(binaryData.getHashValue());
    }

    @Test
    public void testList() throws FileNotFoundException, RepositoryException {
    	ObjectListing listObjects = S3Client.getInstance().listObjects();
    	Iterator<S3ObjectSummary> iterator = listObjects.getObjectSummaries().iterator();
    	while(iterator.hasNext()){
    		S3ObjectSummary next = iterator.next();
    		System.out.println(next.getKey());
    	}
    }
}

