package com.silentgo.utils;

import com.silentgo.utils.logger.Logger;
import com.silentgo.utils.logger.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Project : silentgo
 * com.silentgo.kit
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 16/8/20.
 */
public class PropKit {

    private static final Logger LOGGER = LoggerFactory.getLog(PropKit.class);

    Properties properties = null;

    public PropKit(String fileName) {
        this(fileName, "utf-8");
    }

    public PropKit(String fileName, String encoding) {
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inputStream == null)
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }


    public Object getObject(String key) {
        return properties.getProperty(key);
    }


    public String getValue(String key) {
        return properties.containsKey(key) ? (String) properties.get(key) : null;
    }

    public String getValue(String key, String defaultValue) {
        return properties.containsKey(key) ? (String) properties.get(key) : defaultValue;
    }


    public Integer getInt(String key) {
        String value = getValue(key);
        return value == null ? (Integer) null : Integer.valueOf(value);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Integer.valueOf(value);
    }


    public Long getLong(String key) {
        String value = getValue(key);
        return value == null ? (Long) null : Long.valueOf(value);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Long.valueOf(value);
    }

    public Boolean getBool(String key) {
        String value = getValue(key);
        return value == null ? (Boolean) null : Boolean.valueOf(value);
    }

    public Boolean getBool(String key, Boolean defaultValue) {
        String value = getValue(key);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

}
