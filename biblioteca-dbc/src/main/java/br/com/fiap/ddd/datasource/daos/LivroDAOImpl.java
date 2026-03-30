package br.com.fiap.ddd.datasource.daos;


import br.com.fiap.ddd.exceptions.DatabaseException;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Livro;
import br.com.fiap.ddd.model.enums.StatusLivro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================
 *  PADRÃO: DAO (Data Access Object) — Implementação JDBC
 *  Classe: LivroDAOImpl
 * ============================================================
 *  Implementação concreta do ILivroDAO usando JDBC puro.
 *  - Usa PreparedStatement (evita SQL Injection)
 *  - try-with-resources (auto-close de Connection/Statement)
 *  - Retorna ID gerado (RETURN_GENERATED_KEYS)
 *
 *  FIAP - Disciplina DDD
 *  Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 */
public class LivroDAOImpl implements ILivroDAO {

    private static final Logger log = LoggerFactory.getLogger(LivroDAOImpl.class);

    // ============================================================
    //  SQL Constants
    // ============================================================
    private static final String SQL_INSERT =
            "INSERT INTO livros (titulo, autor, isbn, editora, ano_pub, categoria_id, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE livros SET titulo=?, autor=?, isbn=?, editora=?, ano_pub=?, " +
                    "categoria_id=?, status=? WHERE id=?";

    private static final String SQL_DELETE =
            "DELETE FROM livros WHERE id=?";

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM livros WHERE id=?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM livros ORDER BY titulo";

    private static final String SQL_FIND_BY_ISBN =
            "SELECT * FROM livros WHERE isbn=?";

    private static final String SQL_FIND_BY_AUTOR =
            "SELECT * FROM livros WHERE autor LIKE ? ORDER BY titulo";

    private static final String SQL_FIND_BY_TITULO =
            "SELECT * FROM livros WHERE titulo LIKE ? ORDER BY titulo";

    private static final String SQL_FIND_BY_CATEGORIA =
            "SELECT * FROM livros WHERE categoria_id=? ORDER BY titulo";

    private static final String SQL_FIND_BY_STATUS =
            "SELECT * FROM livros WHERE status=? ORDER BY titulo";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE livros SET status=? WHERE id=?";

    private static final String SQL_COUNT =
            "SELECT COUNT(*) FROM livros";

    private static final String SQL_EXISTS_ISBN =
            "SELECT COUNT(*) FROM livros WHERE isbn=?";

    // ============================================================
    //  SAVE — Insere livro e retorna com ID gerado
    // ============================================================
    @Override
    public Livro save(Livro livro) {
        log.debug("Inserindo livro: {}", livro.getTitulo());
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setString(4, livro.getEditora());
            if (livro.getAnoPub() != null) ps.setInt(5, livro.getAnoPub());
            else                           ps.setNull(5, Types.INTEGER);
            if (livro.getCategoriaId() != null) ps.setInt(6, livro.getCategoriaId());
            else                                ps.setNull(6, Types.INTEGER);
            ps.setString(7, livro.getStatus().name());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new DatabaseException("Nenhuma linha inserida para Livro.");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    livro.setId(generatedKeys.getInt(1));
                    log.info("Livro '{}' inserido com ID={}", livro.getTitulo(), livro.getId());
                }
            }
            return livro;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar livro: " + e.getMessage(), e);
        }
    }

    // ============================================================
    //  UPDATE
    // ============================================================
    @Override
    public void update(Livro livro) {
        log.debug("Atualizando livro ID={}", livro.getId());
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setString(4, livro.getEditora());
            if (livro.getAnoPub() != null) ps.setInt(5, livro.getAnoPub());
            else                           ps.setNull(5, Types.INTEGER);
            if (livro.getCategoriaId() != null) ps.setInt(6, livro.getCategoriaId());
            else                                ps.setNull(6, Types.INTEGER);
            ps.setString(7, livro.getStatus().name());
            ps.setInt(8, livro.getId());

            ps.executeUpdate();
            log.info("Livro ID={} atualizado.", livro.getId());

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar livro ID=" + livro.getId(), e);
        }
    }

    // ============================================================
    //  DELETE
    // ============================================================
    @Override
    public void delete(Integer id) {
        log.debug("Deletando livro ID={}", id);
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            log.info("Livro ID={} deletado. Linhas afetadas: {}", id, rows);

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar livro ID=" + id, e);
        }
    }

    // ============================================================
    //  FIND BY ID
    // ============================================================
    @Override
    public Optional<Livro> findById(Integer id) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar livro ID=" + id, e);
        }
        return Optional.empty();
    }

    // ============================================================
    //  FIND ALL
    // ============================================================
    @Override
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) livros.add(mapRowToLivro(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar livros.", e);
        }
        return livros;
    }

    // ============================================================
    //  FIND BY ISBN
    // ============================================================
    @Override
    public Optional<Livro> findByIsbn(String isbn) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ISBN)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar livro pelo ISBN: " + isbn, e);
        }
        return Optional.empty();
    }

    // ============================================================
    //  FIND BY AUTOR (busca parcial com LIKE)
    // ============================================================
    @Override
    public List<Livro> findByAutor(String autor) {
        return findByStringLike(SQL_FIND_BY_AUTOR, autor);
    }

    // ============================================================
    //  FIND BY TITULO (busca parcial com LIKE)
    // ============================================================
    @Override
    public List<Livro> findByTitulo(String titulo) {
        return findByStringLike(SQL_FIND_BY_TITULO, titulo);
    }

    // ============================================================
    //  FIND BY CATEGORIA
    // ============================================================
    @Override
    public List<Livro> findByCategoria(Integer categoriaId) {
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_CATEGORIA)) {

            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) livros.add(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar livros por categoria.", e);
        }
        return livros;
    }

    // ============================================================
    //  FIND BY STATUS
    // ============================================================
    @Override
    public List<Livro> findByStatus(StatusLivro status) {
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_STATUS)) {

            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) livros.add(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar livros por status.", e);
        }
        return livros;
    }

    // ============================================================
    //  UPDATE STATUS
    // ============================================================
    @Override
    public void updateStatus(Integer id, StatusLivro novoStatus) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            ps.setString(1, novoStatus.name());
            ps.setInt(2, id);
            ps.executeUpdate();
            log.info("Status do livro ID={} atualizado para {}", id, novoStatus);

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar status do livro ID=" + id, e);
        }
    }

    // ============================================================
    //  COUNT ALL
    // ============================================================
    @Override
    public long countAll() {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar livros.", e);
        }
        return 0L;
    }

    // ============================================================
    //  EXISTS BY ISBN
    // ============================================================
    @Override
    public boolean existsByIsbn(String isbn) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_ISBN)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar ISBN: " + isbn, e);
        }
        return false;
    }

    // ============================================================
    //  Helpers privados
    // ============================================================

}