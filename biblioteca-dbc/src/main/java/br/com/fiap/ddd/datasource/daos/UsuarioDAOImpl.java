package br.com.fiap.ddd.datasource.daos;

import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.fiap.ddd.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements IUsuarioDAO {
    private static final Logger log = LoggerFactory.getLogger(UsuarioDAOImpl.class);

    private static final String SQL_INSERT =
            "INSERT INTO usuarios (nome, email, telefone, ativo) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE usuarios SET nome=?, email=?, telefone=?, ativo=? WHERE id=?";
    private static final String SQL_DELETE =
            "DELETE FROM usuarios WHERE id=?";
    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM usuarios WHERE id=?";
    private static final String SQL_FIND_ALL =
            "SELECT * FROM usuarios ORDER BY nome";
    private static final String SQL_FIND_BY_EMAIL =
            "SELECT * FROM usuarios WHERE email=?";
    private static final String SQL_FIND_ATIVOS =
            "SELECT * FROM usuarios WHERE ativo=1 ORDER BY nome";
    private static final String SQL_EXISTS_EMAIL =
            "SELECT COUNT(*) FROM usuarios WHERE email=?";
    private static final String SQL_SET_ATIVO =
            "UPDATE usuarios SET ativo=? WHERE id=?";
    private static final String SQL_COUNT =
            "SELECT COUNT(*) FROM usuarios";

    @Override
    public Usuario save(Usuario usuario) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString (1, usuario.getNome());
            ps.setString (2, usuario.getEmail());
            ps.setString (3, usuario.getTelefone());
            ps.setBoolean(4, usuario.isAtivo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                    log.info("Usuário '{}' inserido com ID={}", usuario.getNome(), usuario.getId());
                }
            }
            return usuario;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Usuario usuario) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString (1, usuario.getNome());
            ps.setString (2, usuario.getEmail());
            ps.setString (3, usuario.getTelefone());
            ps.setBoolean(4, usuario.isAtivo());
            ps.setInt    (5, usuario.getId());
            ps.executeUpdate();
            log.info("Usuário ID={} atualizado.", usuario.getId());

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar usuário ID=" + id, e);
        }
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário ID=" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários.", e);
        }
        return lista;
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por email.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAtivos() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ATIVOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários ativos.", e);
        }
        return lista;
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_EMAIL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar email.", e);
        }
        return false;
    }

    @Override
    public void setAtivo(Integer id, boolean ativo) {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_ATIVO)) {

            ps.setBoolean(1, ativo);
            ps.setInt    (2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao alterar status ativo do usuário.", e);
        }
    }

    @Override
    public long countAll() {
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao contar usuários.", e);
        }
        return 0L;
    }

}
