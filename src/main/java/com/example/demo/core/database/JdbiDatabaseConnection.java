package com.example.demo.core.database;

import com.example.demo.core.utils.PropertiesUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
public class JdbiDatabaseConnection {
    private static JdbiDatabaseConnection instance;
    private final Jdbi jdbi;
    private static final ThreadLocal<Handle> handleThreadLocal = new ThreadLocal<>();

    private JdbiDatabaseConnection() throws ClassNotFoundException {
        System.out.println("Starting JDBI Database connection initialization...");
        
        try {
            String driver = PropertiesUtils.getProperty("db.driver");
            System.out.println("Database driver: " + driver);
            Class.forName(driver);
            System.out.println("Database driver loaded successfully");

            HikariDataSource dataSource = initHikariCP();
            System.out.println("HikariCP data source created");
            
            jdbi = Jdbi.create(dataSource);
            System.out.println("JDBI Database connection initialized successfully!");
        } catch (Exception e) {
            System.err.println("Error initializing JDBI Database connection: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static JdbiDatabaseConnection getInstance() throws ClassNotFoundException {
        System.out.println("JdbiDatabaseConnection.getInstance() called");
        if (instance == null) {
            System.out.println("Creating new JdbiDatabaseConnection instance...");
            instance = new JdbiDatabaseConnection();
        } else {
            System.out.println("Using existing JdbiDatabaseConnection instance");
        }
        return instance;
    }

    private HikariDataSource initHikariCP() {
        System.out.println("Initializing HikariCP...");
        HikariConfig config = new HikariConfig();
        
        String url = PropertiesUtils.getProperty("db.url");
        String username = PropertiesUtils.getProperty("db.username");
        String password = PropertiesUtils.getProperty("db.password");
        
        System.out.println("Database URL: " + url);
        System.out.println("Database Username: " + username);
        System.out.println("Database Password: " + (password != null ? "[HIDDEN]" : "null"));
        
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        HikariDataSource dataSource = new HikariDataSource(config);
        System.out.println("HikariCP initialized successfully");
        return dataSource;
    }

    public static Handle openHandle() {
        System.out.println("Opening database handle...");
        try {
            Handle handle = getInstance().jdbi.open();
            handleThreadLocal.set(handle);
            System.out.println("Database handle opened successfully");
            return handle;
        } catch (ClassNotFoundException e) {
            System.err.println("Error opening database handle: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getException());
        }
    }

    public static void closeHandle() {
        try {
            Handle handle = handleThreadLocal.get();
            if (handle != null) {
                handle.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
