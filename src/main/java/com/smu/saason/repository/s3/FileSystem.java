package com.smu.saason.repository.s3;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

public interface FileSystem {

	public File getRootDir() throws RepositoryException;
	public File getFile(String hashcode) throws RepositoryException;
	public ReadableByteChannelWrapper getReadableChannel(String hashcode) throws RepositoryException;
	public WritableByteChannel getWritableChannel(String hashcode) throws RepositoryException;
	public OutputStream getOutputStream(String hashcode) throws RepositoryException;
	public InputStream getInputStream(String hashcode) throws RepositoryException;
	public boolean exists(String hashcode)throws RepositoryException;
	public void delete(String hashcode)throws RepositoryException;
	public long length(String hashcode)throws RepositoryException;

}
