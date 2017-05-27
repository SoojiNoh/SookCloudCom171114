package com.smu.saason.repository.s3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.UUID;

public class FileChannelBufferInputStream extends InputStream {

	private static int BUFFER_SIZE = 1024 * 4;
    byte[] buffer = new byte[BUFFER_SIZE];
	private int bufferedInputStreamLimit = 1024 * 1024 * 400;
	private String tempUploadDir = System.getProperty("java.io.tmpdir");
	private String tempFileName = UUID.randomUUID().toString();
	private int totalRead;
	
	ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private InputStream inputStream;
	private FileChannel channel;
	
	public FileChannelBufferInputStream(InputStream in) {
		if(in instanceof BufferedInputStream) {
			inputStream = in;
		} else {
			inputStream = new BufferedInputStream(in);
		}
	}
	
	public FileChannelBufferInputStream(InputStream in, String tempUploadDir) {
		this(in);
		this.tempUploadDir = tempUploadDir;
	}
	
	public FileChannelBufferInputStream(InputStream in, int bufferedInputstreamLimit) {
		this(in);
		this.bufferedInputStreamLimit = bufferedInputstreamLimit;
	}
	
	public FileChannelBufferInputStream(InputStream in, String tempUploadDir, int bufferedInputstreamLimit) {
		this(in);
		this.tempUploadDir = tempUploadDir;
		this.bufferedInputStreamLimit = bufferedInputstreamLimit;
	}
	
	 public int read(byte b[]) throws IOException {
		 if(isFileInputStream()) {
			 return readFileChannel(b);
		 } else {
			 return readBuffedredInputStream(b);
		 }
    }

	private int readFileChannel(byte[] b) throws IOException {
		int read = 0;
		if((read=channel.read(byteBuffer)) != -1) {
		    if (read != 0) {
		    	byteBuffer.position(0);
		    	byteBuffer.limit(read);
		    	while(byteBuffer.hasRemaining()) {
		    		byteBuffer.get(b, 0, Math.min( byteBuffer.remaining(), b.length));
		    	}
		    	byteBuffer.clear();
		    }
		}
		return read;
	}

	private int readBuffedredInputStream(byte[] b) throws IOException {
		int read = inputStream.read(b);
		if (read > 0) {
			totalRead += read;
			if (totalRead > bufferedInputStreamLimit) {
				createTempFile();
				switchInpuStream();
				getFileChanelAndSetPostion();
			}
		}
		return read;
	}

	private boolean isFileInputStream() {
		return inputStream instanceof FileInputStream;
	}

	private void getFileChanelAndSetPostion() throws IOException {
		channel  = ((FileInputStream) inputStream).getChannel();
		channel.position(totalRead);
	}

	private void switchInpuStream() throws IOException {
		inputStream.close();
		inputStream = null;
		inputStream = new FileInputStream(new File(tempUploadDir + "/"+ tempFileName));
	}

	private void createTempFile() throws IOException {
		File tempFile = new File(tempUploadDir + "/"+ tempFileName);
		if (tempFile.exists()) {
			tempFile.delete();
		}
		tempFile.createNewFile();

		inputStream.reset();
		inputStream.mark(-1);

		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		
		WritableByteChannel outChannel = null;
		try {
			outChannel = new FileOutputStream(tempFile).getChannel();
			do {
				read = inputStream.read(buffer);
				if (read > 0) {
					byteBuffer.put(buffer, 0, read);
					byteBuffer.flip();
					outChannel.write(byteBuffer);
				}
				byteBuffer.clear();
			} while (read > 0);
		} finally {
			outChannel.close();
		}
	}

	@Override
	public synchronized void mark(int readlimit) {
		if(inputStream instanceof BufferedInputStream) {
			inputStream.mark(readlimit);
		}
	}
	
	@Override
	public synchronized void reset() throws IOException {
		totalRead = 0;
		if(isFileInputStream()) {
			channel = ((FileInputStream) inputStream).getChannel();
			channel.position(0);
		} else {
			inputStream.reset();
		}
	}

	@Override
	public void close() throws IOException {
		if(inputStream != null) {
			inputStream.close();
		}
		File file = new File(tempUploadDir + "/"+ tempFileName);
		file.delete();
		if(file.exists()) {
			file.delete();
		}
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}
	
}