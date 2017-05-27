package com.smu.saason.repository.s3;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by mint on 11/04/2017.
 */

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.smu.saason.config.ConfigProperties;


@Component
@ComponentScan
public class S3Client {
    private static ConfigProperties configProperties;

    private static S3Client instance;

    private AmazonS3 amazonS3;
    private String bucketName;

    @Autowired
    private S3Client(ConfigProperties configProperties){
        this.configProperties = configProperties;

        this.bucketName = configProperties.bucketName;

        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard().withCredentials(new ClasspathPropertiesFileCredentialsProvider());
        s3ClientBuilder.setRegion(configProperties.region);
        amazonS3 = s3ClientBuilder.build();

        if (!amazonS3.doesBucketExist(bucketName)){
            amazonS3.createBucket(bucketName);
        }

        instance = this;

    }

    public static S3Client getInstance(){
        if(instance == null){
            instance = new S3Client(configProperties);
        }

        return instance;
    }

    public String getBucketName() {
        return bucketName;
    }

//	public AmazonS3 getAmazonS3(){
//		return amazonS3;
//	}

    public boolean existsObject(String workspaceId, String intermediatePath, String hashcode) {
        return amazonS3.doesObjectExist(bucketName, getKey(workspaceId, intermediatePath, hashcode));
    }

    public S3Object getObject(String workspaceId, String intermediatePath, String hashcode) {
        return amazonS3.getObject(bucketName, getKey(workspaceId, intermediatePath, hashcode));
    }


    public void deleteObject(String workspaceId, String intermediatePath, String hashcode) {
        amazonS3.deleteObject(bucketName, getKey(workspaceId, intermediatePath, hashcode));
    }

    public void putObject(String workspaceId, String intermediatePath, String hashcode, File file) {
        amazonS3.putObject(bucketName, getKey(workspaceId, intermediatePath, hashcode), file);
    }

    private String getKey(String workspaceId, String intermediatePath, String hashcode){
        return new StringBuilder()
                .append(workspaceId)
                .append("/")
                .append(intermediatePath)
                .append("/")
                .append(hashcode).toString();
    }

    public void putObject(String key, File file) {
        amazonS3.putObject(bucketName, key, file);
    }

    public ObjectListing listObjects() {
    	return amazonS3.listObjects(bucketName);
    }



    public static void main(String[] args) throws IOException {
        ClasspathPropertiesFileCredentialsProvider credentialProvider = new ClasspathPropertiesFileCredentialsProvider();
        System.out.println(credentialProvider.getCredentials().getAWSAccessKeyId());
        System.out.println(credentialProvider.getCredentials().getAWSSecretKey());

        Region region = Region.getRegion(Regions.AP_NORTHEAST_2);

//		AmazonS3Client s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
//		Region apNE1 = Region.getRegion(Regions.AP_NORTHEAST_2);
//		s3.setRegion(apNE1);

        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard().withCredentials(credentialProvider);
        s3ClientBuilder.setRegion(region.getName());
        AmazonS3 s3 = s3ClientBuilder.build();

        System.out.println(region.getName());

//		s3.putObject("mygaiatest-bucket", "wid/aa/bb/cc/aabbccdd", "contentncontent");
//		S3Object object = s3.getObject("mygaiatest-bucket", "wid/aa/bb/cc/aabbccdd");
//		S3ObjectInputStream is = object.getObjectContent();
//		String string = IOUtils.toString(is);
//		System.out.println(string);


        List<Bucket> listBuckets = s3.listBuckets();
        for (Bucket bucket : listBuckets) {
            System.out.println(bucket.getName());
        }
    }




}