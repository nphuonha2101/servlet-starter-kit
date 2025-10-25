package com.example.demo.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load properties file 
 * This class is used to load the properties file from the classpath
 * and return the property value by key
 */
public class PropertiesUtils {
    private static final String APPLICATION_PROPERTIES_PATH = "/properties/application.properties";
    private static Properties properties;
    private static PropertiesUtils instance = null;

    private PropertiesUtils() {
        properties = new Properties();
        loadPropertiesFile();
    }

    private static PropertiesUtils getInstance() {
        if (instance == null) {
            instance = new PropertiesUtils();
        }
        return instance;
    }

    /**
     * Load properties file
     */
    private void loadPropertiesFile() {
        System.out.println();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(APPLICATION_PROPERTIES_PATH);
            properties.load(inputStream);

        } catch (Exception e) {
            throw new RuntimeException("Cannot load properties file: " + APPLICATION_PROPERTIES_PATH);
        }
    }

    public static String getProperty(String key) {
        getInstance();
        return properties.getProperty(key);
    }

    public static Properties getProperties() {
        getInstance();
        return properties;
    }
}

