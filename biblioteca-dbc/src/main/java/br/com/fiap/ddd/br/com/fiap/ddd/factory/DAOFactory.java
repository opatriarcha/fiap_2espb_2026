package br.com.fiap.ddd.br.com.fiap.ddd.factory;

import br.com.fiap.ddd.datasource.daos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

    /**
     * ============================================================
     *  PADRÃO: FACTORY
     *  Classe: DAOFactory
     * ============================================================
     *  Centraliza a CRIAÇÃO de objetos DAO, desacoplando quem
     *  usa o DAO de quem o implementa.
     *
     *  Utiliza um registro interno (Map) para armazenar as
     *  implementações, permitindo:
     *   - Troca de implementação sem alterar código cliente
     *   - Registro de novos DAOs em runtime
     *   - Fácil substituição por mocks em testes
     *
     *  FIAP - Disciplina DDD
     *  Prof. Mestre Orlando C. Patriarcha
     * ============================================================
     *
     *  COMO USAR:
     *  <pre>
     *    ILivroDAO livroDAO = DAOFactory.getDAO(ILivroDAO.class);
     *    IUsuarioDAO usuarioDAO = DAOFactory.getDAO(IUsuarioDAO.class);
     *  </pre>
     *
     *  REGISTRAR UM DAO CUSTOMIZADO (para testes / mock):
     *  <pre>
     *    DAOFactory.registerDAO(ILivroDAO.class, new LivroDAOMockImpl());
     *  </pre>
     */
    public class DAOFactory {

        private static final Logger log = LoggerFactory.getLogger(DAOFactory.class);

        // ============================================================
        //  Registro de DAOs: Interface → Implementação
        // ============================================================
        private static final Map<Class<?>, Object> DAO_REGISTRY = new HashMap<>();

        // ============================================================
        //  Bloco estático: registra os DAOs padrão ao carregar a classe
        // ============================================================
        static {
            log.info(">> Inicializando DAOFactory — registrando DAOs padrão...");
            registerDAO(ILivroDAO.class,      new LivroDAOImpl());
            registerDAO(IUsuarioDAO.class,    new UsuarioDAOImpl());
            registerDAO(IEmprestimoDAO.class, new EmprestimoDAOImpl());
            log.info(">> DAOFactory pronto. DAOs registrados: {}", DAO_REGISTRY.size());
        }

        // ============================================================
        //  Construtor privado — classe utilitária, sem instanciação
        // ============================================================
        private DAOFactory() {}

        // ============================================================
        //  Registra (ou substitui) uma implementação de DAO
        //  Útil para injetar mocks em testes unitários
        // ============================================================
        public static <T> void registerDAO(Class<T> daoInterface, T implementation) {
            if (daoInterface == null || implementation == null) {
                throw new IllegalArgumentException("Interface e implementação não podem ser nulos.");
            }
            log.debug(">> DAOFactory: registrando {} → {}",
                    daoInterface.getSimpleName(),
                    implementation.getClass().getSimpleName());
            DAO_REGISTRY.put(daoInterface, implementation);
        }

        // ============================================================
        //  Obtém um DAO pelo tipo da interface
        //  Lança exceção se o DAO não estiver registrado
        // ============================================================
        @SuppressWarnings("unchecked")
        public static <T> T getDAO(Class<T> daoInterface) {
            T dao = (T) DAO_REGISTRY.get(daoInterface);
            if (dao == null) {
                throw new IllegalArgumentException(
                        "DAO não registrado para a interface: " + daoInterface.getName()
                                + ". Use DAOFactory.registerDAO() para registrá-lo."
                );
            }
            return dao;
        }

        // ============================================================
        //  Atalhos tipados (conveniência)
        // ============================================================
        public static ILivroDAO      getLivroDAO()      {
            return getDAO(ILivroDAO.class);
        }
        public static IUsuarioDAO    getUsuarioDAO()    {
            return getDAO(IUsuarioDAO.class);
        }
        public static IEmprestimoDAO getEmprestimoDAO() {
            return getDAO(IEmprestimoDAO.class);
        }

        /** Lista os DAOs registrados (útil para debug) */
        public static void listarDAOsRegistrados() {
            log.info(">> DAOs registrados na DAOFactory:");
            DAO_REGISTRY.forEach((iface, impl) ->
                    log.info("   {} → {}", iface.getSimpleName(), impl.getClass().getSimpleName())
            );
        }
}
