package com.smu.saason.repository.s3;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.model.S3Object;
import com.smu.saason.config.ConfigProperties;

@Component
@ComponentScan
public class S3FileSystem implements FileSystem {
	private static Logger logger = LoggerFactory.getLogger(S3FileSystem.class);
	private File dataRoot;

	private String workspaceId;

	@Autowired
	public S3FileSystem(ConfigProperties configProperties) throws RepositoryException {
		this.workspaceId = configProperties.workspaceId;

		this.dataRoot = new File(configProperties.location);
		if (!dataRoot.exists()){
			dataRoot.mkdirs();
		}

	}

	public String getWorkspceId(){
		return workspaceId;
	}

	@Override
	public File getRootDir() throws RepositoryException {
		return dataRoot;
	}

	@Override
	public File getFile(String hashcode) throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			throw new IllegalArgumentException("hashcode is required.");
		}

		String intermediatePath = getIntermediatePath(hashcode);
//		String fullPath = intermediatePath + "/" + hashcode;
		
		File directory = new File(getRootDir(), getIntermediatePath(hashcode));
		if (!directory.exists()){
			directory.mkdirs();
			if(!directory.exists()){
				synchronized (S3FileSystem.class) {
					directory.mkdirs();
					int max = 0;
					while(!directory.exists() ){
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {

						}
						if(max++ > 10){
							break;
						}
					}
				}
			}
		}

		File file = new File(directory, hashcode);

		if (!file.exists()){
			BufferedOutputStream bos = null;
			InputStream is = null;
			try {
				if (S3Client.getInstance().existsObject(workspaceId, intermediatePath, hashcode)){
					file.createNewFile();

					S3Object s3Object = S3Client.getInstance().getObject(workspaceId, intermediatePath, hashcode);
//					S3Object s3Object = S3Client.getInstance().getAmazonS3().getObject(bucketName, hashcode);
					is = s3Object.getObjectContent();
					bos = new BufferedOutputStream(new FileOutputStream(file));
					IOUtils.copyLarge(is, bos);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(bos);
			}
		}

		return file;
	}




	public String getIntermediatePath(String hashcode) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.substring(hashcode, 0, 2));
		sb.append("/");
		sb.append(StringUtils.substring(hashcode, 2, 4));
		sb.append("/");
		sb.append(StringUtils.substring(hashcode, 4, 6));
		return sb.toString();
	}

	public String getFilePath(String hashcode) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.substring(hashcode, 0, 2));
		sb.append("/");
		sb.append(StringUtils.substring(hashcode, 2, 4));
		sb.append("/");
		sb.append(StringUtils.substring(hashcode, 4, 6));
		sb.append("/");
		sb.append(hashcode);
		return sb.toString();
	}



	@Override
	public ReadableByteChannelWrapper getReadableChannel(String hashcode)
			throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			throw new IllegalArgumentException("hashcode is required.");
		}

		try {
			final File file = getFile(hashcode);
			final FileChannel channel = new FileInputStream(file).getChannel();
			return new ReadableByteChannelWrapper() {

				@Override
				public boolean isOpen() {
					return channel.isOpen();
				}

				@Override
				public void close() throws IOException {
					channel.close();
				}

				@Override
				public int read(ByteBuffer dst) throws IOException {
					return channel.read(dst);
				}

				@Override
				public long size() throws IOException {
					return file.length();
				}
			};
		} catch (IOException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public WritableByteChannel getWritableChannel(final String hashcode)
			throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			throw new IllegalArgumentException("hashcode is required.");
		}

		final String intermediatePath = getIntermediatePath(hashcode);
		final File file = getFile(hashcode);
		try {
			final FileChannel channel = new FileOutputStream(file).getChannel();


			return new WritableByteChannel() {

				@Override
				public boolean isOpen() {
					return channel.isOpen();
				}

				@Override
				public void close() throws IOException {
					channel.close();
					S3Client.getInstance().putObject(workspaceId, intermediatePath, hashcode, file);
				}

				@Override
				public int write(ByteBuffer src) throws IOException {
					return channel.write(src);
				}
			};
		} catch (IOException e) {
//			try {
//				if (file.exists()){
//					file.delete();	
//				}
//				
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
			throw new RepositoryException(e);
		}
	}

	@Override
	public OutputStream getOutputStream(String hashcode)
			throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			throw new IllegalArgumentException("hashcode is required.");
		}

		try {
			return new FileOutputStream(getFile(hashcode));
		} catch (IOException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public boolean exists(String hashcode) throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			return false;
		}
		
		File file = new File(getRootDir(), getFilePath(hashcode));
		if (file.exists()){
			return true;
		}
		String intermediatePath = getIntermediatePath(hashcode);
		return S3Client.getInstance().existsObject(workspaceId, intermediatePath, hashcode);
	}

	@Override
	public void delete(String hashcode) throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			return;
		}

		File file = new File(getRootDir(), getFilePath(hashcode));
		if (file.exists()){
			file.delete();
		}

		String intermediatePath = getIntermediatePath(hashcode);
		if (S3Client.getInstance().existsObject(workspaceId, intermediatePath, hashcode)){
			S3Client.getInstance().deleteObject(workspaceId, intermediatePath, hashcode);
		}
	}

	@Override
	public InputStream getInputStream(String hashcode) throws RepositoryException {
		if (StringUtils.isBlank(hashcode)) {
			throw new IllegalArgumentException("hashcode is required.");
		}

		try {
			return new FileInputStream(getFile(hashcode));
		} catch (IOException e) {
			throw new RepositoryException(e);
		}
	}
	@Override
	public long length(String hashcode) throws RepositoryException {
		return getFile(hashcode).length();
	}

}
