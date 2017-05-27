package com.smu.saason.repository.s3;


import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public interface ReadableByteChannelWrapper extends ReadableByteChannel{

	long size() throws IOException;
}
