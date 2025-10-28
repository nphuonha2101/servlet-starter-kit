package com.nphuonha.servletkit.core.database;

import com.nphuonha.servletkit.core.utils.PropertiesUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
public class JdbiDatabaseConnection {
    private static JdbiDatabaseConnection instance;
    private final Jdbi jdbi;
    private static final ThreadLocal<Handle> handleThreadLocal = new ThreadLocal<>();

    private JdbiDatabaseConnection() throws ClassNotFoundException {
        try {
            String driver = PropertiesUtils.getProperty("db.driver");
            Class.forName(driver);

            HikariDataSource dataSource = initHikariCP();
            jdbi = Jdbi.create(dataSource);
            System.out.println("✓ Database connection initialized");
        } catch (Exception e) {
            System.err.println("✗ Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static JdbiDatabaseConnection getInstance() throws ClassNotFoundException {
        if (instance == null) {
            instance = new JdbiDatabaseConnection();
        }
        return instance;
    }

    private HikariDataSource initHikariCP() {
        HikariConfig config = new HikariConfig();
        
        String url = PropertiesUtils.getProperty("db.url");
        String username = PropertiesUtils.getProperty("db.username");
        String password = PropertiesUtils.getProperty("db.password");
        
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

    /**
     * Get the Jdbi instance
     * @return Jdbi instance
     */
    public Jdbi getJdbi() {
        return jdbi;
    }

    public static Handle openHandle() {
        try {
            Handle handle = getInstance().jdbi.open();
            handleThreadLocal.set(handle);
            return handle;
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error opening database handle: " + e.getMessage());
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
