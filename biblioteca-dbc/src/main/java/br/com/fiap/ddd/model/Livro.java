package br.com.fiap.ddd.model;

import br.com.fiap.ddd.model.enums.StatusLivro;
import java.time.LocalDateTime;

/**
 * ============================================================
 *  Model: Livro
 *  FIAP - Disciplina DDD
 *  Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 * Representa a entidade Livro no domínio da aplicação.
 */
public class Livro {

    private Integer id;
    private String  titulo;
    private String  autor;
    private String  isbn;
    private String  editora;
    private Integer anoPub;
    private Integer categoriaId;
    private StatusLivro status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // ---- Constructors ----

    public Livro() {
        this.status = StatusLivro.DISPONIVEL;
    }

    public Livro(String titulo, String autor, String isbn,
                 String editora, Integer anoPub, Integer categoriaId) {
        this.titulo      = titulo;
        this.autor       = autor;
        this.isbn        = isbn;
        this.editora     = editora;
        this.anoPub      = anoPub;
        this.categoriaId = categoriaId;
        this.status      = StatusLivro.DISPONIVEL;
    }

    // ---- Getters & Setters ----

    public Integer getId()                        { return id; }
    public void    setId(Integer id)              { this.id = id; }

    public String  getTitulo()                    { return titulo; }
    public void    setTitulo(String titulo)       { this.titulo = titulo; }

    public String  getAutor()                     { return autor; }
    public void    setAutor(String autor)         { this.autor = autor; }

    public String  getIsbn()                      { return isbn; }
    public void    setIsbn(String isbn)           { this.isbn = isbn; }

    public String  getEditora()                   { return editora; }
    public void    setEditora(String editora)     { this.editora = editora; }

    public Integer getAnoPub()                    { return anoPub; }
    public void    setAnoPub(Integer anoPub)      { this.anoPub = anoPub; }

    public Integer getCategoriaId()               { return categoriaId; }
    public void    setCategoriaId(Integer id)     { this.categoriaId = id; }

    public StatusLivro getStatus()                { return status; }
    public void        setStatus(StatusLivro s)   { this.status = s; }

    public LocalDateTime getCriadoEm()            { return criadoEm; }
    public void          setCriadoEm(LocalDateTime c) { this.criadoEm = c; }

    public LocalDateTime getAtualizadoEm()        { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime a)  { this.atualizadoEm = a; }

    public boolean isDisponivel() {
        return StatusLivro.DISPONIVEL.equals(this.status);
    }

    @Override
    public String toString() {
        return "Livro{id=" + id + ", titulo='" + titulo + "', autor='" + autor
                + "', isbn='" + isbn + "', status=" + status + "}";
    }
}
