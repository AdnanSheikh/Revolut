package com.revolut.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by adnan on 8/15/2018.
 */
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final String RESOURCE_FILENAME = "application.properties";

    private static Properties properties;

    static {
        properties = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(RESOURCE_FILENAME);
        try{
            properties.load(inputStream);
        } catch (IOException ex) {
            logger.error("Error while reading configuration:",ex);
        }
    }

    public static Integer getIntProperty(final String key){
        return Integer.parseInt(properties.getProperty(key));
    }

    public static String getProperty(final String key){
        return properties.getProperty(key);
    }

    public static String getProperty(final String prefix, final String key){
        if(!StringUtils.isEmpty(prefix)){
            return properties.getProperty(prefix.concat(".").concat(key));
        }
        return properties.getProperty(key);
    }
}