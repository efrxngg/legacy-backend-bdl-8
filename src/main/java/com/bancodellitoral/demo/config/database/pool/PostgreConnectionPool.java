package com.bancodellitoral.demo.config.database.pool;

import com.bancodellitoral.demo.config.database.hikary.DatabaseConfig;
import com.bancodellitoral.demo.config.database.hikary.DatabaseConnectionPool;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Singleton
public class PostgreConnectionPool {

    private DatabaseConnectionPool pool;

    @PostConstruct
    private void initializePool() {
        // Configuración usando builder pattern
        DatabaseConfig config = getDatabaseConfig();
        Properties props = getProperties(config);

        this.pool = new DatabaseConnectionPool(config, props);

        // Verificar salud del pool
        if (pool.isHealthy()) {
            System.out.println("Pool inicializado correctamente");
            System.out.println("Estadísticas: " + pool.getPoolStats());
        }
    }

    public Connection getConnection() throws SQLException {
        return this.pool.getConnection();
    }

    private static DatabaseConfig getDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        return config
                .setDriverClassName("org.postgresql.Driver")
                .setHost("localhost")
                .setPort(5432)
                .setDatabase("postgres")
                .setUsername("postgres")
                .setPassword("postgres")
                .setPoolName("custom-postgres-ds-pool")
                .setApplicationName("reporteria")
                .setMaxPoolSize(10)
                .setMinIdleConnections(5);
    }

    private static Properties getProperties(DatabaseConfig config) {
        Properties props = new Properties();
        props.setProperty("socketTimeout", "30");
        props.setProperty("loginTimeout", "30");
        props.setProperty("connectTimeout", "30");
        props.setProperty("cancelSignalTimeout", "10");
        props.setProperty("tcpKeepAlive", "true");
        props.setProperty("ApplicationName", config.getApplicationName());
        props.setProperty("prepareThreshold", "5");
        props.setProperty("preparedStatementCacheQueries", "256");
        props.setProperty("preparedStatementCacheSizeMiB", "5");
        props.setProperty("defaultRowFetchSize", "1000");
        return props;
    }

}
