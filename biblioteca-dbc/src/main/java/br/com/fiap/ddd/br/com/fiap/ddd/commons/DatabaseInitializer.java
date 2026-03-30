package br.com.fiap.ddd.br.com.fiap.ddd.commons;


import br.com.fiap.ddd.exceptions.DatabaseException;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ============================================================
 *  Utilitário: DatabaseInitializer
 *  FIAP - Disciplina DDD | Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 *  Inicializa o banco H2 executando o schema.sql ao iniciar.
 *  Usa H2 RunScript para parsing correto de SQL (comentários,
 *  múltiplos statements, etc.)
 *
 *  Como usar:
 *  <pre>
 *    DatabaseInitializer.init();     // executa schema.sql padrão
 *    DatabaseInitializer.cleanData();  // trunca tabelas (entre testes)
 *    DatabaseInitializer.dropAll();    // dropa tudo (@AfterAll)
 *  </pre>
 */
public class DatabaseInitializer {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static volatile boolean initialized = false;


    private DatabaseInitializer() {}

    /**
     * Executa o schema.sql no banco H2. Thread-safe — só roda uma vez.
     */
    public static synchronized void init() {
        init("schema.sql");
    }

    /**
     * Executa um script SQL do classpath.
     * Usa H2 RunScript — suporta comentários, múltiplos statements, etc.
     */
    public static synchronized void init(String scriptName) {
        System.out.println("inicializando banco -------------");
        if (initialized) {
            log.debug("DatabaseInitializer: banco já inicializado.");
            return;
        }
        log.info(">> DatabaseInitializer: executando '{}'...", scriptName);

        try (InputStream is = DatabaseInitializer.class
                .getClassLoader().getResourceAsStream(scriptName)) {
            if (is == null)
                throw new DatabaseException("Script não encontrado no classpath: " + scriptName);

            try (Connection conn = ConnectionManager.getInstance().getConnection();
                 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                // H2 RunScript faz o parsing correto: lida com comentários,
                // múltiplos statements e ponto-e-vírgula dentro de strings.
                RunScript.execute(conn, reader);
            }
        } catch (SQLException | IOException e) {
            throw new DatabaseException("Erro ao executar script '" + scriptName + "': " + e.getMessage(), e);
        }

        initialized = true;
        log.info(">> DatabaseInitializer: banco pronto. ------------------------------------------");
    }

    /**
     * Permite re-inicializar (útil no @BeforeEach dos testes de integração).
     */
    public static synchronized void reset() {
        initialized = false;
    }

    /**
     * Trunca todas as tabelas preservando a estrutura.
     * Chame no @BeforeEach dos testes de integração para isolar cada teste.
     */
    public static void cleanData() {
        log.debug("DatabaseInitializer: limpando dados das tabelas...");
        String[] stmts = {
                "SET REFERENTIAL_INTEGRITY FALSE",
                "TRUNCATE TABLE emprestimos",
                "TRUNCATE TABLE livros",
                "TRUNCATE TABLE usuarios",
                "TRUNCATE TABLE categorias",
                "SET REFERENTIAL_INTEGRITY TRUE"
        };
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             Statement st = conn.createStatement()) {
            for (String ddl : stmts) {
                st.execute(ddl);
            }
            log.debug("DatabaseInitializer: dados limpos.");
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao limpar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Remove todas as tabelas (DROP ALL OBJECTS).
     * Útil no @AfterAll para liberar recursos do H2.
     */
    public static void dropAll() {
        log.debug("DatabaseInitializer: executando DROP ALL OBJECTS...");
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DROP ALL OBJECTS");
            initialized = false;
            log.info(">> DatabaseInitializer: todas as tabelas removidas.");
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao executar DROP ALL OBJECTS.", e);
        }
    }
}
