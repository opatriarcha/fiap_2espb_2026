package br.com.fiap.ddd.infrastructure.connection;


import br.com.fiap.ddd.exceptions.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    // Garante visibilidade entre threads
    private static volatile ConnectionManager instance;
    private final HikariDataSource dataSource;

    private ConnectionManager(){
        log.info(">> Inicializando ConnectionManager (Singleton) com HikariCP...");
        Properties props = this.loadProperties();

        HikariConfig config = this.buildHikariConfig(props);
        this.dataSource = new HikariDataSource(config);

        log.info(">> Pool de conexões '{}' iniciado com sucesso. MaxPoolSize={}",
                config.getPoolName(), config.getMaximumPoolSize());
    }

    private Properties loadProperties() {
        Properties props =  new Properties();
        try(InputStream is = this.getClass().getResourceAsStream("/db.properties") ){
            if( is == null )
                 throw new DatabaseException("Arquivo db.properties nao encontrado no classpath!!!");
            props.load(is);
        }catch( IOException ex ){
            throw new DatabaseException("Erro ao carregar db.properties");
        }
         return props;
    }

    public Connection getConnection(){
        try{
            return dataSource.getConnection();
        }catch( Exception ex ){
            throw new DatabaseException("Falha ao obter conexao do pool Hikari", ex);
        }
    }

    public static ConnectionManager getInstance(){
        if( instance == null ){
            synchronized (ConnectionManager.class ){
                if( instance == null )
                    instance = new ConnectionManager();
            }
        }
        return instance;
    }

    public void closePool(){
        if( this.dataSource != null && !this.dataSource.isClosed()){
            this.dataSource.close();
            log.info(">> Pool de Conexoes encerrado corretamente.");
        }
    }

    private HikariConfig buildHikariConfig(Properties props) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl         (props.getProperty("db.url"));
        config.setUsername        (props.getProperty("db.username"));
        config.setPassword        (props.getProperty("db.password"));
        config.setDriverClassName (props.getProperty("db.driver"));
        config.setPoolName        (props.getProperty("hikari.pool.name",    "FIAP-Pool"));
        config.setMaximumPoolSize (Integer.parseInt(props.getProperty("hikari.maximum.pool.size", "10")));
        config.setMinimumIdle     (Integer.parseInt(props.getProperty("hikari.minimum.idle",       "2")));
        config.setConnectionTimeout(Long.parseLong (props.getProperty("hikari.connection.timeout","30000")));
        config.setIdleTimeout     (Long.parseLong  (props.getProperty("hikari.idle.timeout",    "600000")));
        config.setMaxLifetime     (Long.parseLong  (props.getProperty("hikari.max.lifetime",   "1800000")));
        config.setAutoCommit      (Boolean.parseBoolean(props.getProperty("hikari.auto.commit", "true")));
        return config;
    }

    public int getActiveConnections()  { return dataSource.getHikariPoolMXBean().getActiveConnections(); }
    public int getIdleConnections()    { return dataSource.getHikariPoolMXBean().getIdleConnections(); }
    public int getTotalConnections()   { return dataSource.getHikariPoolMXBean().getTotalConnections(); }
}
