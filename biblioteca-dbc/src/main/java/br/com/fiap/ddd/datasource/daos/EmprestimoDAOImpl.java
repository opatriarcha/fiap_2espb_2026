package br.com.fiap.ddd.datasource.daos;

import br.com.fiap.ddd.exceptions.DatabaseException;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Emprestimo;
import br.com.fiap.ddd.model.enums.StatusEmprestimo;
import br.com.fiap.ddd.model.enums.StatusLivro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmprestimoDAOImpl implements IEmprestimoDAO {
    private static final Logger log = LoggerFactory.getLogger(EmprestimoDAOImpl.class);

    private static final String SQL_INSERT =
            "INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, data_prevista, status) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE emprestimos SET livro_id=?, usuario_id=?, data_emprestimo=?, " +
                    "data_prevista=?, data_devolucao=?, status=?, observacao=? WHERE id=?";

    private static final String SQL_FIND_BY_ID =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "WHERE e.id=?";

    private static final String SQL_FIND_ALL =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "ORDER BY e.data_emprestimo DESC";

    private static final String SQL_FIND_BY_USUARIO =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "WHERE e.usuario_id=? ORDER BY e.data_emprestimo DESC";

    private static final String SQL_FIND_BY_LIVRO =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "WHERE e.livro_id=?";

    private static final String SQL_FIND_ATIVOS =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "WHERE e.status='ATIVO'";

    private static final String SQL_FIND_ATRASADOS =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
                    "FROM emprestimos e " +
                    "JOIN livros l ON l.id = e.livro_id " +
                    "JOIN usuarios u ON u.id = e.usuario_id " +
                    "WHERE e.status='ATIVO' AND e.data_prevista < CURRENT_DATE";

    private static final String SQL_DEVOLVER =
            "UPDATE emprestimos SET data_devolucao=?, status='DEVOLVIDO' WHERE id=?";

    private static final String SQL_UPDATE_STATUS_LIVRO =
            "UPDATE livros SET status=? WHERE id=?";

    private static final String SQL_COUNT_ATIVOS =
            "SELECT COUNT(*) FROM emprestimos WHERE status='ATIVO'";


    @Override
    public Emprestimo save(Emprestimo emp){
        try(Connection conn = ConnectionManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, emp.getLivroId());
            ps.setInt(2, emp.getUsuarioId());
            ps.setDate(3, Date.valueOf(emp.getDataEmprestimo()));
            ps.setDate(4, Date.valueOf(emp.getDataPrevista()));
            ps.setString(5, emp.getStatus().name());

            ps.executeUpdate();

            try( ResultSet rs = ps.getGeneratedKeys()){
                if( rs.next() )
                    emp.setId(rs.getInt(1));
            }
            return emp;
        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao salvar emprestimo", ex);
        }
    }

    @Override
    public void update(Emprestimo emp){
        try(Connection conn = ConnectionManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)){
            ps.setInt(1, emp.getLivroId());
            ps.setInt(2, emp.getUsuarioId());
            ps.setDate(3, Date.valueOf(emp.getDataEmprestimo()));
            ps.setDate(4, Date.valueOf(emp.getDataPrevista()));
            if( emp.getDataDevolucao() != null  )
                ps.setDate(5, Date.valueOf(emp.getDataDevolucao()));
            else
                ps.setNull(5, Types.DATE);

            ps.setString(6, emp.getStatus().name());
            ps.setString(7, emp.getObservacao());
            ps.setInt(8, emp.getId());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Erro ao realizar update do emprestimo", ex);
        }
    }

    @Override
    public List<Emprestimo> findAll(){
        return this.executeListQuery(SQL_FIND_ALL, ps-> {});
    }

    @Override
    public List<Emprestimo> findByUsuario(Integer usuarioId){
        return this.executeListQuery(SQL_FIND_BY_USUARIO, ps -> ps.setInt(1, usuarioId));
    }

    @Override
    public List<Emprestimo> findByLivro(Integer livroId){
        return this.executeListQuery(SQL_FIND_BY_LIVRO, ps -> ps.setInt(1, livroId));
    }

    @Override
    public List<Emprestimo> findAtivos(){

        return this.executeListQuery(SQL_FIND_ATIVOS, ps->{});
    }
    @Override
    public List<Emprestimo> findAtrasados(){
        return this.executeListQuery(SQL_FIND_ATRASADOS, ps->{});
    }

    // ============================================================
    //  DEVOLVER — atualiza devolução e libera o livro
    // ============================================================
    
    @Override
    public void devolver(Integer emprestimoId) {
        // Busca o empréstimo para obter o livroId
        Emprestimo emp = findById(emprestimoId)
                .orElseThrow(() -> new DatabaseException(
                        "Empréstimo não encontrado: ID=" + emprestimoId));

        Connection conn = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Registra devolução no empréstimo
            try (PreparedStatement ps = conn.prepareStatement(SQL_DEVOLVER)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setInt (2, emprestimoId);
                ps.executeUpdate();
            }

            // 2. Libera o livro (status → DISPONIVEL)
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS_LIVRO)) {
                ps.setString(1, StatusLivro.DISPONIVEL.name());
                ps.setInt   (2, emp.getLivroId());
                ps.executeUpdate();
            }

            conn.commit(); // Confirma transação
            log.info("Devolução concluída: empréstimo ID={}, livro ID={} liberado.",
                    emprestimoId, emp.getLivroId());

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {
                    log.error("Erro no rollback da devolução.", ex);
                }
            }
            throw new DatabaseException("Erro na devolução do empréstimo.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    log.error("Erro ao fechar conexão.", e);
                }
            }
        }
    }

    @Override
    public Optional<Emprestimo> findById(Integer id) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar empréstimo ID=" + id, e);
        }
        return Optional.empty();
    }

    // ============================================================
    //  REALIZAR EMPRÉSTIMO — Transação ACID completa
    //  1. Insere empréstimo
    //  2. Atualiza status do livro → EMPRESTADO
    //  3. COMMIT (ou ROLLBACK se falhar)
    // ============================================================
    
    @Override
    public Emprestimo realizarEmprestimo(Integer livroId, Integer usuarioId, int diasPrazo) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false); // ← Inicia transação

            LocalDate hoje     = LocalDate.now();
            LocalDate prevista = hoje.plusDays(diasPrazo);

            // PASSO 1: Insere o empréstimo
            Emprestimo emp = new Emprestimo(livroId, usuarioId, hoje, prevista);
            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT,
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt   (1, emp.getLivroId());
                ps.setInt   (2, emp.getUsuarioId());
                ps.setDate  (3, Date.valueOf(emp.getDataEmprestimo()));
                ps.setDate  (4, Date.valueOf(emp.getDataPrevista()));
                ps.setString(5, emp.getStatus().name());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) emp.setId(rs.getInt(1));
                }
            }

            // PASSO 2: Atualiza status do livro → EMPRESTADO
            try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS_LIVRO)) {
                ps.setString(1, StatusLivro.EMPRESTADO.name());
                ps.setInt   (2, livroId);
                ps.executeUpdate();
            }

            conn.commit(); // ← COMMIT: persiste tudo
            log.info("Empréstimo ID={} realizado: livroId={}, usuarioId={}, devolver até {}",
                    emp.getId(), livroId, usuarioId, prevista);
            return emp;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // ← ROLLBACK: desfaz tudo
                    log.warn("ROLLBACK executado: falha no empréstimo.");
                } catch (SQLException ex) {
                    log.error("Erro no rollback.", ex);
                }
            }
            throw new DatabaseException("Erro ao realizar empréstimo.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    log.error("Erro ao fechar conexão.", e);
                }
            }
        }
    }

    // ============================================================
    //  COUNT ATIVOS
    // ============================================================
    
    @Override
    public long countAtivos() {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ATIVOS);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar empréstimos ativos.", e);
        }
        return 0L;
    }


}
