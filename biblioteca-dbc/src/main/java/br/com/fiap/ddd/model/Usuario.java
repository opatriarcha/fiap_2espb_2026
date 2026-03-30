package br.com.fiap.ddd.model;

import java.time.LocalDateTime;

/**
 * ============================================================
 *  Model: Usuario
 *  FIAP - Disciplina DDD
 *  Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 */
public class Usuario {

    private Integer id;
    private String  nome;
    private String  email;
    private String  telefone;
    private boolean ativo;
    private LocalDateTime criadoEm;

    // ---- Constructors ----

    public Usuario() {
        this.ativo = true;
    }

    public Usuario(String nome, String email, String telefone) {
        this.nome     = nome;
        this.email    = email;
        this.telefone = telefone;
        this.ativo    = true;
    }

    // ---- Getters & Setters ----

    public Integer getId()                         { return id; }
    public void    setId(Integer id)               { this.id = id; }

    public String  getNome()                       { return nome; }
    public void    setNome(String nome)            { this.nome = nome; }

    public String  getEmail()                      { return email; }
    public void    setEmail(String email)          { this.email = email; }

    public String  getTelefone()                   { return telefone; }
    public void    setTelefone(String t)           { this.telefone = t; }

    public boolean isAtivo()                       { return ativo; }
    public void    setAtivo(boolean ativo)         { this.ativo = ativo; }

    public LocalDateTime getCriadoEm()             { return criadoEm; }
    public void setCriadoEm(LocalDateTime c)       { this.criadoEm = c; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome
                + "', email='" + email + "', ativo=" + ativo + "}";
    }
}
