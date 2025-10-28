package com.nphuonha.servletkit.core.listeners;

import com.nphuonha.servletkit.core.database.JdbiDatabaseConnection;
import com.nphuonha.servletkit.core.database.MigrationHandler;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Listener to run database migrations automatically when the application starts
 * Configured in web.xml to run after CDI initialization
 */
public class DatabaseMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("============================================");
        System.out.println("Starting Database Migration on Application Startup");
        System.out.println("============================================");
        
        try {
            // Initialize database connection and run migrations
            JdbiDatabaseConnection dbConnection = JdbiDatabaseConnection.getInstance();
            var jdbi = dbConnection.getJdbi();
            MigrationHandler.runMigrations(jdbi);
            
            System.out.println("Database migration completed successfully!");
            System.out.println("============================================");
        } catch (Exception e) {
            System.err.println("============================================");
            System.err.println("CRITICAL: Database migration failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("============================================");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application context destroyed");
    }
}

