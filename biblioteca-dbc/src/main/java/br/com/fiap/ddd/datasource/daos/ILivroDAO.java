package br.com.fiap.ddd.datasource.daos;

import br.com.fiap.ddd.exceptions.DatabaseException;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Livro;
import br.com.fiap.ddd.model.enums.StatusLivro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ILivroDAO {
    // ============================================================
    //  SAVE — Insere livro e retorna com ID gerado
    // ============================================================
    Livro save(Livro livro);

    // ============================================================
    //  UPDATE
    // ============================================================
    void update(Livro livro);

    // ============================================================
    //  DELETE
    // ============================================================
    void delete(Integer id);

    // ============================================================
    //  FIND BY ID
    // ============================================================
    Optional<Livro> findById(Integer id);

    // ============================================================
    //  FIND ALL
    // ============================================================
    List<Livro> findAll();

    // ============================================================
    //  FIND BY ISBN
    // ============================================================
    Optional<Livro> findByIsbn(String isbn);

    // ============================================================
    //  FIND BY AUTOR (busca parcial com LIKE)
    // ============================================================
    List<Livro> findByAutor(String autor);

    // ============================================================
    //  FIND BY TITULO (busca parcial com LIKE)
    // ============================================================
    List<Livro> findByTitulo(String titulo);

    // ============================================================
    //  FIND BY CATEGORIA
    // ============================================================
    List<Livro> findByCategoria(Integer categoriaId);

    // ============================================================
    //  FIND BY STATUS
    // ============================================================
    List<Livro> findByStatus(StatusLivro status);

    // ============================================================
    //  UPDATE STATUS
    // ============================================================
    void updateStatus(Integer id, StatusLivro novoStatus);

    // ============================================================
    //  COUNT ALL
    // ============================================================
    long countAll();

    // ============================================================
    //  EXISTS BY ISBN
    // ============================================================
    boolean existsByIsbn(String isbn);

    /**
     * Reutilizável para buscas LIKE em colunas String
     */
    default List<Livro> findByStringLike(String sql, String valor) {
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + valor + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) livros.add(mapRowToLivro(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro em busca LIKE: " + e.getMessage(), e);
        }
        return livros;
    }

    /**
     * Mapeia linha do ResultSet → objeto Livro
     */
    default Livro mapRowToLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setIsbn(rs.getString("isbn"));
        livro.setEditora(rs.getString("editora"));
        livro.setAnoPub(rs.getObject("ano_pub", Integer.class));
        livro.setCategoriaId(rs.getObject("categoria_id", Integer.class));
        livro.setStatus(StatusLivro.valueOf(rs.getString("status")));
        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) livro.setCriadoEm(criadoEm.toLocalDateTime());
        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) livro.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        return livro;
    }
}
