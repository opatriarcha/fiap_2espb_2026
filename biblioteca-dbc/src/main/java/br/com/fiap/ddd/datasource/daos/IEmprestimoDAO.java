package br.com.fiap.ddd.datasource.daos;

import br.com.fiap.ddd.exceptions.DatabaseException;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Emprestimo;
import br.com.fiap.ddd.model.enums.StatusEmprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IEmprestimoDAO {
    Emprestimo save(Emprestimo emp);

    void update(Emprestimo emp);

    default List<Emprestimo> executeListQuery(String sql, StatementSetter setter) {
        List<Emprestimo> resultset = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setter.set(ps);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    resultset.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Erro em query de empréstimos.", ex);
        }
        return resultset;
    }

    // ============================================================
    //  Mapeia ResultSet → Emprestimo
    // ============================================================
    default Emprestimo mapRow(ResultSet rs) throws SQLException {
        Emprestimo e = new Emprestimo();
        e.setId(rs.getInt("id"));
        e.setLivroId(rs.getInt("livro_id"));
        e.setUsuarioId(rs.getInt("usuario_id"));
        Date dataEmp = rs.getDate("data_emprestimo");
        if (dataEmp != null) e.setDataEmprestimo(dataEmp.toLocalDate());
        Date dataPrev = rs.getDate("data_prevista");
        if (dataPrev != null)
            e.setDataPrevista(dataPrev.toLocalDate());
        Date dataDev = rs.getDate("data_devolucao");
        if (dataDev != null) e.setDataDevolucao(dataDev.toLocalDate());
        e.setStatus(StatusEmprestimo.valueOf(rs.getString("status")));
        e.setObservacao(rs.getString("observacao"));
        // Colunas do JOIN
        try {
            e.setTituloLivro(rs.getString("titulo_livro"));
        } catch (SQLException ignored) {

        }
        try {
            e.setNomeUsuario(rs.getString("nome_usuario"));
        } catch (SQLException ignored) {
        }
        Timestamp ts = rs.getTimestamp("criado_em");
        if (ts != null)
            e.setCriadoEm(ts.toLocalDateTime());
        return e;
    }

    List<Emprestimo> findAll();

    List<Emprestimo> findByUsuario(Integer usuarioId);

    List<Emprestimo> findByLivro(Integer livroId);

    List<Emprestimo> findAtivos();

    List<Emprestimo> findAtrasados();

    void devolver(Integer emprestimoId);

    Optional<Emprestimo> findById(Integer id);

    Emprestimo realizarEmprestimo(Integer livroId, Integer usuarioId, int diasPrazo);

    long countAtivos();

    @FunctionalInterface
    public interface StatementSetter {
        void set(PreparedStatement ps) throws SQLException;
    }
}
