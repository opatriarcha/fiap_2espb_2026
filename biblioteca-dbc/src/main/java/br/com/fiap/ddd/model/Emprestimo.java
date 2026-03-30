package br.com.fiap.ddd.model;

import br.com.fiap.ddd.model.enums.StatusEmprestimo;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ============================================================
 *  Model: Emprestimo
 *  FIAP - Disciplina DDD
 *  Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 */
public class Emprestimo {

    private Integer         id;
    private Integer         livroId;
    private Integer         usuarioId;
    private LocalDate       dataEmprestimo;
    private LocalDate      dataPrevista;
    private LocalDate       dataDevolucao;
    private StatusEmprestimo status;
    private String          observacao;
    private LocalDateTime   criadoEm;

    // Dados desnormalizados para exibição
    private String tituloLivro;
    private String nomeUsuario;

    // ---- Constructors ----

    public Emprestimo() {}

    public Emprestimo(Integer livroId, Integer usuarioId,
                      LocalDate dataEmprestimo, LocalDate dataPrevista) {
        this.livroId        = livroId;
        this.usuarioId      = usuarioId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista   = dataPrevista;
        this.status         = StatusEmprestimo.ATIVO;
    }

    // ---- Getters & Setters ----

    public Integer getId()                            { return id; }
    public void    setId(Integer id)                  { this.id = id; }

    public Integer getLivroId()                       { return livroId; }
    public void    setLivroId(Integer livroId)        { this.livroId = livroId; }

    public Integer getUsuarioId()                     { return usuarioId; }
    public void    setUsuarioId(Integer usuarioId)    { this.usuarioId = usuarioId; }

    public LocalDate getDataEmprestimo()              { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate d)        { this.dataEmprestimo = d; }

    public LocalDate getDataPrevista()                { return dataPrevista; }
    public void setDataPrevista(LocalDate d)          { this.dataPrevista = d; }

    public LocalDate getDataDevolucao()               { return dataDevolucao; }
    public void setDataDevolucao(LocalDate d)         { this.dataDevolucao = d; }

    public StatusEmprestimo getStatus()               { return status; }
    public void setStatus(StatusEmprestimo s)         { this.status = s; }

    public String getObservacao()                     { return observacao; }
    public void   setObservacao(String obs)           { this.observacao = obs; }

    public LocalDateTime getCriadoEm()                { return criadoEm; }
    public void setCriadoEm(LocalDateTime c)          { this.criadoEm = c; }

    public String getTituloLivro()                    { return tituloLivro; }
    public void   setTituloLivro(String t)            { this.tituloLivro = t; }

    public String getNomeUsuario()                    { return nomeUsuario; }
    public void   setNomeUsuario(String n)            { this.nomeUsuario = n; }

    public boolean isAtrasado() {
        return dataDevolucao == null
                && dataPrevista != null
                && LocalDate.now().isAfter(dataPrevista);
    }

    @Override
    public String toString() {
        return "Emprestimo{id=" + id + ", livroId=" + livroId
                + ", usuarioId=" + usuarioId
                + ", status=" + status
                + ", dataPrevista=" + dataPrevista + "}";
    }
}
