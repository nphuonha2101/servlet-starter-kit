package com.example.demo.core.database;

import com.example.demo.core.utils.PropertiesUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MigrationHandler {
    private static final String MIGRATION_TABLE = "schema_migrations";
    private static final String MIGRATION_PATH = "/migrations/";
    private static boolean initialized = false;

    /**
     * Run all pending migrations in the database
     */
    public static void runMigrations(Jdbi jdbi) {
        if (initialized) {
            System.out.println("Migrations already initialized, skipping...");
            return;
        }

        System.out.println("Starting database migrations...");
        
        // Get database name from config
        String databaseName = getDatabaseNameFromConfig();
        System.out.println("Target database: " + databaseName);
        
        try (Handle handle = jdbi.open()) {
            // Create database if not exists
            createDatabaseIfNotExists(handle, databaseName);
            
            // Use the database
            useDatabase(handle, databaseName);
            
            // Create migration tracking table if it doesn't exist
            createMigrationTable(handle);
            
        // Get list of migration files from resources
        Set<String> migrationFiles = getMigrationFiles();
        
        if (migrationFiles.isEmpty()) {
            initialized = true;
            return;
        }
        
        // Sort migration files by name (they should be prefixed with timestamp)
        migrationFiles = migrationFiles.stream()
                .sorted()
                .collect(Collectors.toSet());
        
        int executedCount = 0;
        int skippedCount = 0;
        
        // Run each migration that hasn't been run yet
        for (String migrationFile : migrationFiles) {
            if (!isMigrationExecuted(handle, migrationFile)) {
                System.out.println("  ✓ Running migration: " + migrationFile);
                try {
                    // Start transaction
                    handle.begin();
                    
                    // Run migration within transaction
                    runMigration(handle, migrationFile);
                    
                    // Mark as executed
                    markMigrationAsExecuted(handle, migrationFile);
                    
                    // Commit transaction
                    handle.commit();
                    executedCount++;
                    
                    System.out.println("  ✓ Migration completed: " + migrationFile);
                } catch (Exception e) {
                    // Rollback on error
                    System.err.println("  ✗ Migration failed: " + migrationFile);
                    System.err.println("  Error: " + e.getMessage());
                    
                    try {
                        handle.rollback();
                        System.err.println("  ✓ Transaction rolled back");
                    } catch (Exception rollbackEx) {
                        System.err.println("  ✗ Rollback failed: " + rollbackEx.getMessage());
                    }
                    
                    throw new RuntimeException("Migration failed: " + migrationFile, e);
                }
            } else {
                skippedCount++;
            }
        }
        
        initialized = true;
        
        if (executedCount > 0) {
            System.out.println("✓ " + executedCount + " migration(s) executed, " + skippedCount + " skipped");
        } else {
            System.out.println("✓ All " + skippedCount + " migration(s) already up to date");
        }
            
        } catch (Exception e) {
            System.err.println("Error running migrations: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Migration failed", e);
        }
    }

    /**
     * Get database name from config
     */
    private static String getDatabaseNameFromConfig() {
        // Try db.name first, then parse from db.url
        String dbName = PropertiesUtils.getProperty("db.name");
        if (dbName != null && !dbName.isEmpty()) {
            return dbName;
        }
        
        // Parse from db.url if db.name is not set
        String dbUrl = PropertiesUtils.getProperty("db.url");
        if (dbUrl != null && dbUrl.contains("/")) {
            String[] parts = dbUrl.split("/");
            if (parts.length > 1) {
                String lastPart = parts[parts.length - 1];
                if (lastPart.contains("?")) {
                    return lastPart.split("\\?")[0];
                }
                return lastPart;
            }
        }
        
        // Default database name
        return "app_db";
    }

    /**
     * Create database if not exists
     */
    private static void createDatabaseIfNotExists(Handle handle, String databaseName) {
        try {
            String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName + 
                        " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            handle.execute(sql);
        } catch (Exception e) {
            // Database might already exist
        }
    }

    /**
     * Use the specified database
     */
    private static void useDatabase(Handle handle, String databaseName) {
        String sql = "USE " + databaseName;
        handle.execute(sql);
    }

    /**
     * Create the migration tracking table
     */
    private static void createMigrationTable(Handle handle) {
        String sql = "CREATE TABLE IF NOT EXISTS " + MIGRATION_TABLE + " (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "migration_name VARCHAR(255) NOT NULL UNIQUE, " +
                "executed_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
        
        handle.execute(sql);
    }

    /**
     * Check if a migration has already been executed
     */
    private static boolean isMigrationExecuted(Handle handle, String migrationName) {
        String sql = "SELECT COUNT(*) FROM " + MIGRATION_TABLE + " WHERE migration_name = ?";
        Integer count = handle.createQuery(sql)
                .bind(0, migrationName)
                .mapTo(Integer.class)
                .one();
        return count > 0;
    }

    /**
     * Mark a migration as executed
     */
    private static void markMigrationAsExecuted(Handle handle, String migrationName) {
        String sql = "INSERT INTO " + MIGRATION_TABLE + " (migration_name) VALUES (?) " +
                "ON DUPLICATE KEY UPDATE executed_at = CURRENT_TIMESTAMP";
        handle.createUpdate(sql)
                .bind(0, migrationName)
                .execute();
    }

    /**
     * Run a single migration file
     */
    private static void runMigration(Handle handle, String migrationFile) {
        // Read migration file from resources
        String resourcePath = MIGRATION_PATH + migrationFile;
        try (InputStream inputStream = MigrationHandler.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Migration file not found: " + resourcePath);
            }
            
            // Read file content
            String migrationSql;
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                migrationSql = scanner.useDelimiter("\\A").next();
            }
            
            // Execute migration SQL
            // Handle multiple statements (split by semicolon)
            String[] statements = migrationSql.split(";");
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        handle.execute(statement);
                    } catch (Exception e) {
                        // Ignore "database doesn't exist" errors in CREATE DATABASE statement
                        if (!e.getMessage().contains("database") || !statement.contains("CREATE DATABASE")) {
                            throw e;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute migration: " + migrationFile, e);
        }
    }

    /**
     * Get list of migration files from the resources
     * Automatically scans /migrations/ folder for all .sql files
     */
    private static Set<String> getMigrationFiles() {
        Set<String> migrations = new HashSet<>();
        
        try {
            // Try to scan the migrations directory
            URL migrationsUrl = MigrationHandler.class.getResource(MIGRATION_PATH);
            
            if (migrationsUrl == null) {
                System.err.println("Warning: migrations folder not found at: " + MIGRATION_PATH);
                return migrations;
            }
            
            List<String> files = new ArrayList<>();
            
            // Check if we're running from JAR or from file system
            if (migrationsUrl.getProtocol().equals("jar")) {
                // Running from JAR file
                String jarPath = migrationsUrl.getPath().substring(5, migrationsUrl.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        String name = entries.nextElement().getName();
                        if (name.startsWith("migrations/") && name.endsWith(".sql") && !name.contains("README")) {
                            String fileName = name.substring("migrations/".length());
                            if (!fileName.isEmpty()) {
                                files.add(fileName);
                            }
                        }
                    }
                }
            } else {
                // Running from file system (development)
                Path migrationsPath;
                if (Paths.get(System.getProperty("user.dir")) != null) {
                    migrationsPath = Paths.get("src/main/resources/migrations");
                    if (!migrationsPath.toFile().exists()) {
                        migrationsPath = Paths.get(migrationsUrl.toURI());
                    }
                } else {
                    migrationsPath = Paths.get(migrationsUrl.toURI());
                }
                
                if (migrationsPath.toFile().exists() && migrationsPath.toFile().isDirectory()) {
                    try (Stream<Path> paths = java.nio.file.Files.list(migrationsPath)) {
                        paths.filter(p -> p.toString().endsWith(".sql"))
                                .map(p -> p.getFileName().toString())
                                .forEach(files::add);
                    }
                }
            }
            
            migrations.addAll(files);
            
        } catch (Exception e) {
            System.err.println("Error scanning migration files: " + e.getMessage());
            e.printStackTrace();
        }
        
        return migrations;
    }
}

