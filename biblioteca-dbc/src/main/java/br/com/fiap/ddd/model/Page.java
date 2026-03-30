package br.com.fiap.ddd.model;

import java.util.List;

/**
 * ============================================================
 *  Utilitário de Paginação - Page<T>
 *  FIAP - Disciplina DDD
 *  Prof. Mestre Orlando C. Patriarcha
 * ============================================================
 * Encapsula resultado paginado retornado pelo Repository.
 */
public class Page<T> {

    private final List<T> conteudo;
    private final int     pagina;
    private final int     tamanhoPagina;
    private final long    totalElementos;
    private final int     totalPaginas;

    public Page(List<T> conteudo, int pagina, int tamanhoPagina, long totalElementos) {
        this.conteudo       = conteudo;
        this.pagina         = pagina;
        this.tamanhoPagina  = tamanhoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas   = (int) Math.ceil((double) totalElementos / tamanhoPagina);
    }

    public List<T> getConteudo()       { return conteudo; }
    public int     getPagina()         { return pagina; }
    public int     getTamanhoPagina()  { return tamanhoPagina; }
    public long    getTotalElementos() { return totalElementos; }
    public int     getTotalPaginas()   { return totalPaginas; }
    public boolean isUltimaPagina()    { return pagina >= totalPaginas - 1; }
    public boolean isPrimeiraPagina()  { return pagina == 0; }
    public boolean isEmpty()           { return conteudo.isEmpty(); }

    @Override
    public String toString() {
        return "Page{pagina=" + pagina + "/" + totalPaginas
                + ", total=" + totalElementos
                + ", itens=" + conteudo.size() + "}";
    }
}
