package com.bancodellitoral.demo.config.database.hikary;

/**
 * Configuración del pool de conexiones
 */
public class DatabaseConfig {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String driverClassName;
    private String poolName;
    private String applicationName;

    // Configuración del pool
    private int maxPoolSize = 20;
    private int minIdleConnections = 5;

    // Configuración SSL
    private boolean sslEnabled = false;
    private String sslMode = "require";

    public String getHost() {
        return host;
    }

    public DatabaseConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public DatabaseConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public DatabaseConfig setDatabase(String database) {
        this.database = database;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DatabaseConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DatabaseConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public DatabaseConfig setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getPoolName() {
        return poolName;
    }

    public DatabaseConfig setPoolName(String poolName) {
        this.poolName = poolName;
        return this;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public DatabaseConfig setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public DatabaseConfig setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public int getMinIdleConnections() {
        return minIdleConnections;
    }

    public DatabaseConfig setMinIdleConnections(int minIdleConnections) {
        this.minIdleConnections = minIdleConnections;
        return this;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public DatabaseConfig setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
        return this;
    }

    public String getSslMode() {
        return sslMode;
    }

    public DatabaseConfig setSslMode(String sslMode) {
        this.sslMode = sslMode;
        return this;
    }
}