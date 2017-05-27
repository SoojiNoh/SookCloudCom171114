package com.smu.saason;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.smu.saason.config.ConfigProperties;
import com.smu.saason.repository.s3.FileSystem;

/**
 * Created by mint on 13/04/2017.
 */
@Component
public class BeanAccessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static BeanAccessor accessor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        accessor = this;
        this.applicationContext = applicationContext;
    }

    public static BeanAccessor getInstance(){
        return accessor;
    }

    private ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }

    public ConfigProperties getConfigProperties() {
        return accessor.getApplicationContext().getBean(ConfigProperties.class);
    }

    public FileSystem getFileSystem() {
        return accessor.getApplicationContext().getBean(FileSystem.class);
    }
    
    public <T> T getBean(Class<T> clazz) {
    	return accessor.getApplicationContext().getBean(clazz);
    }

    public TempDirectory getTempDirectory() {
        return accessor.getApplicationContext().getBean(TempDirectory.class);
    }

}
