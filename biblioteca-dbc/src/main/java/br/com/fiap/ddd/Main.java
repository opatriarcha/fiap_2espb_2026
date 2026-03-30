package br.com.fiap.ddd;

import br.com.fiap.ddd.br.com.fiap.ddd.commons.DatabaseInitializer;
import br.com.fiap.ddd.br.com.fiap.ddd.factory.DAOFactory;
import br.com.fiap.ddd.infrastructure.connection.ConnectionManager;
import br.com.fiap.ddd.model.Emprestimo;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//        ConnectionManager.getInstance().getConnection();

        DatabaseInitializer.init();
        List<Emprestimo> emprestimos = DAOFactory.getEmprestimoDAO().findAll();
        System.out.println(DAOFactory.getLivroDAO().findAll().size());
        System.out.println(DAOFactory.getUsuarioDAO().findAll().size());


        for(Emprestimo emp : emprestimos ){
            System.out.println(emp);
        }
    }
}
