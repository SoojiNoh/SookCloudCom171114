package com.smu.saason;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smu.saason.config.ConfigProperties;

/**
 * Created by mint on 13/04/2017.
 */

@Component
public class TempDirectory {

    private File uploadDirectory;
    private File tempRootDirectory;

    @Autowired
    private TempDirectory(ConfigProperties configProperties){
        tempRootDirectory = new File(configProperties.temp);
        if (!tempRootDirectory.exists()){
            tempRootDirectory.mkdirs();
        }


        uploadDirectory = new File(tempRootDirectory, "upload");
        if (!uploadDirectory.exists()){
            uploadDirectory.mkdirs();
        }

    }

    public File getUploadDirectory(){
        return uploadDirectory;
    }

    public File getTempRootDirectory(){
        return tempRootDirectory;
    }
}
