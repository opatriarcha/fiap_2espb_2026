package br.com.fiap.ddd.datasource.daos;

import br.com.fiap.ddd.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface IUsuarioDAO {
    Usuario save(Usuario usuario);

    void update(Usuario usuario);

    void delete(Integer id);

    Optional<Usuario> findById(Integer id);

    List<Usuario> findAll();

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAtivos();

    boolean existsByEmail(String email);

    void setAtivo(Integer id, boolean ativo);

    long countAll();

    default Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setTelefone(rs.getString("telefone"));
        u.setAtivo(rs.getBoolean("ativo"));
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null) u.setCriadoEm(ts.toLocalDateTime());
        return u;
    }
}
