package com.smu.saason.repository.s3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.MessageDigest;

import com.smu.saason.BeanAccessor;

/**
 * Created by mint on 12/04/2017.
 */
public class BinaryData {

    private FileSystem fileSystem;
    private String hashcode = null;
    private InputStream is = null;

    public BinaryData(String hashcode){
        this.hashcode = hashcode;
        this.fileSystem = BeanAccessor.getInstance().getFileSystem();
    }


    public BinaryData(File file) throws FileNotFoundException, RepositoryException {
        this(new FileInputStream(file));
    }


    public BinaryData(InputStream is) throws RepositoryException {
        this.fileSystem = BeanAccessor.getInstance().getFileSystem();
        this.is = is;

        if (is instanceof FileInputStream){
            init((FileInputStream)is);
        } else {
            init(new FileChannelBufferInputStream(is, BeanAccessor.getInstance().getTempDirectory().getUploadDirectory().getAbsolutePath(), BeanAccessor.getInstance().getConfigProperties().STREAM_BUFFER_SIZE));
        }
    }



    private void init(FileInputStream in) throws RepositoryException {
        if(in!=null){

            FileChannel inChannel = in.getChannel();
            WritableByteChannel outChannel = null;
            try{
                int read;
                ByteBuffer byteBuffer = ByteBuffer.allocate(BeanAccessor.getInstance().getConfigProperties().BUFFER_SIZE);


                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                while ((read = inChannel.read(byteBuffer)) > 0) {
                    byteBuffer.flip();
                    digest.update(byteBuffer);
                    byteBuffer.clear();
                }
                StringBuilder builder = new StringBuilder();

                byte[] bytes = digest.digest();
                for (int i = 0; i < bytes.length; i++) {
                    builder.append(String.format("%02x", bytes[i] & 0xff));
                }
                hashcode = builder.toString();

                File file = fileSystem.getFile(this.hashcode);
                if(!file.exists()){
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    inChannel.position(0);

                    outChannel = fileSystem.getWritableChannel(this.hashcode);

                    while ((read = inChannel.read(byteBuffer)) > 0) {
                        byteBuffer.flip();
                        outChannel.write(byteBuffer);
                        byteBuffer.clear();
                    }
                }

            }catch(Exception e){
                throw new RepositoryException(e);
            }finally{
                if (inChannel != null){
                    try {
                        inChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outChannel != null){
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void init(FileChannelBufferInputStream in) throws RepositoryException {
        if(in!=null){
            WritableByteChannel outChannel = null;
            byte[] buffer = new byte[BeanAccessor.getInstance().getConfigProperties().BUFFER_SIZE];
            int read;
            ByteBuffer byteBuffer = ByteBuffer.allocate(BeanAccessor.getInstance().getConfigProperties().BUFFER_SIZE);

            try{

                in.mark(Integer.MAX_VALUE);
                StringBuilder builder = new StringBuilder();
                MessageDigest  digest = MessageDigest.getInstance("SHA-256");
                do {

                    read = in.read(buffer);
                    if (read > 0) {
                        byteBuffer.put(buffer, 0,read);
                        byteBuffer.flip();
                        digest.update(byteBuffer);
                    }
                    byteBuffer.clear();
                } while (read>0);

                byte[] bytes = digest.digest();

                for (int i = 0; i < bytes.length; i++) {
                    builder.append(String.format("%02x", bytes[i] & 0xff));
                }
                hashcode = builder.toString();

                in.reset();

                File file = fileSystem.getFile(this.hashcode);
                if(!file.exists()){
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    outChannel = fileSystem.getWritableChannel(this.hashcode);
                    do {

                        read = in.read(buffer);
                        if (read > 0) {
                            byteBuffer.put(buffer, 0,read);
                            byteBuffer.flip();
                            outChannel.write(byteBuffer);
                        }
                        byteBuffer.clear();
                    } while (read>0);
                }



            }catch(Exception e){
                throw new RepositoryException(e);
            }finally{
                if (in != null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outChannel != null){
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getHashValue() {
        return hashcode;
    }

    public File getFile() throws RepositoryException {
        if(this.hashcode!=null){
            return fileSystem.getFile(hashcode);
        }
        return null;
    }

    public ReadableByteChannel getStream() throws ValueFormatException {
        if(this.hashcode!=null){
            try {
                return fileSystem.getReadableChannel(hashcode);
            } catch (RepositoryException e) {
                throw new ValueFormatException(e);
            }
        }
        return null;
    }

    public long getLength() throws RepositoryException {
        if(this.hashcode!=null){
            return fileSystem.getFile(this.hashcode).length();
        }
        return 0;
    }


}
