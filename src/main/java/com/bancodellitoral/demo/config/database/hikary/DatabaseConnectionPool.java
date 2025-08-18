package com.bancodellitoral.demo.config.database.hikary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Pool de conexiones genérico usando HikariCP con PostgreSQL
 * Implementa las mejores prácticas para gestión de conexiones
 */
public class DatabaseConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionPool.class);

    private HikariDataSource dataSource;
    private final DatabaseConfig config;
    private final Properties properties;

    public DatabaseConnectionPool(DatabaseConfig config, Properties properties) {
        this.properties = properties;
        this.config = config;
        initializePool();
    }

    /**
     * Inicializa el pool de conexiones con configuración optimizada
     */
    private void initializePool() {
        try {
            HikariConfig hikariConfig = new HikariConfig();

            // Configuración de conexión básica
            hikariConfig.setJdbcUrl(buildJdbcUrl());
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());
            hikariConfig.setDriverClassName(config.getDriverClassName());

            // Configuración del pool - Mejores prácticas
            hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
            hikariConfig.setMinimumIdle(config.getMinIdleConnections());
            hikariConfig.setConnectionTimeout(TimeUnit.SECONDS.toMillis(30));
            hikariConfig.setIdleTimeout(TimeUnit.MINUTES.toMillis(10));
            hikariConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(30));
            hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(60));

            // Configuración del pool name para identificación
            hikariConfig.setPoolName(config.getPoolName());

            // Configuración de validación de conexiones
            hikariConfig.setConnectionTestQuery("SELECT 1");
            hikariConfig.setValidationTimeout(TimeUnit.SECONDS.toMillis(5));

            // Configuraciones particulares del driver de conexion
            hikariConfig.setDataSourceProperties(properties);

            // Configuraciones adicionales de rendimiento
            hikariConfig.setAutoCommit(false); // Mejor control transaccional
            hikariConfig.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

            // JMX para monitoreo
            hikariConfig.setRegisterMbeans(true);

            this.dataSource = new HikariDataSource(hikariConfig);

            logger.info("Pool de conexiones inicializado exitosamente: {}", config.getPoolName());

        } catch (Exception e) {
            logger.error("Error al inicializar el pool de conexiones", e);
            throw new RuntimeException("No se pudo inicializar el pool de conexiones", e);
        }
    }

    /**
     * Construye la URL JDBC completa
     */
    private String buildJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%d/%s",
                config.getHost(),
                config.getPort(),
                config.getDatabase());
    }

    /**
     * Obtiene una conexión del pool
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("El pool de conexiones no está disponible");
        }

        Connection connection = dataSource.getConnection();
        logger.debug("Conexión obtenida del pool. Conexiones activas: {}",
                dataSource.getHikariPoolMXBean().getActiveConnections());

        return connection;
    }

    /**
     * Ejecuta una operación con manejo automático de conexión
     */
    public <T> T executeWithConnection(ConnectionOperation<T> operation) throws SQLException {
        try (Connection connection = getConnection()) {
            return operation.execute(connection);
        }
    }

    /**
     * Ejecuta una operación con transacción automática
     */
    public <T> T executeInTransaction(TransactionOperation<T> operation) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                T result = operation.execute(connection);
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Obtiene el DataSource para frameworks externos
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Obtiene estadísticas del pool
     */
    public PoolStats getPoolStats() {
        if (dataSource == null) {
            return new PoolStats(0, 0, 0, 0);
        }

        HikariPoolMXBean mxBean = dataSource.getHikariPoolMXBean();
        return new PoolStats(
                mxBean.getActiveConnections(),
                mxBean.getIdleConnections(),
                mxBean.getTotalConnections(),
                mxBean.getThreadsAwaitingConnection()
        );
    }

    /**
     * Verifica la salud del pool
     */
    public boolean isHealthy() {
        try (Connection connection = getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            logger.warn("Health check falló", e);
            return false;
        }
    }

    /**
     * Cierra el pool de conexiones
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Cerrando pool de conexiones: {}", config.getPoolName());
            dataSource.close();
        }
    }

    /**
     * Interface funcional para operaciones con conexión
     */
    @FunctionalInterface
    public interface ConnectionOperation<T> {
        T execute(Connection connection) throws SQLException;
    }

    /**
     * Interface funcional para operaciones transaccionales
     */
    @FunctionalInterface
    public interface TransactionOperation<T> {
        T execute(Connection connection) throws SQLException;
    }

    /**
     * Clase para estadísticas del pool
     */
    public static class PoolStats {
        private final int activeConnections;
        private final int idleConnections;
        private final int totalConnections;
        private final int threadsAwaiting;

        public PoolStats(int active, int idle, int total, int awaiting) {
            this.activeConnections = active;
            this.idleConnections = idle;
            this.totalConnections = total;
            this.threadsAwaiting = awaiting;
        }

        // Getters
        public int getActiveConnections() {
            return activeConnections;
        }

        public int getIdleConnections() {
            return idleConnections;
        }

        public int getTotalConnections() {
            return totalConnections;
        }

        public int getThreadsAwaiting() {
            return threadsAwaiting;
        }

        @Override
        public String toString() {
            return String.format("PoolStats{active=%d, idle=%d, total=%d, awaiting=%d}",
                    activeConnections, idleConnections, totalConnections, threadsAwaiting);
        }
    }
}



