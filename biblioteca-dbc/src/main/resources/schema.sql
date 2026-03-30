-- ============================================================
--  FIAP - Disciplina DDD
--  Script DDL - Biblioteca Digital
--  Banco: H2 (compatível MySQL MODE)
--  Prof. Mestre Orlando C. Patriarcha
-- ============================================================

-- ======================== TABELAS ============================

CREATE TABLE IF NOT EXISTS categorias (
                                          id         INT AUTO_INCREMENT PRIMARY KEY,
                                          nome       VARCHAR(100) NOT NULL,
    descricao  VARCHAR(255),
    criado_em  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_categoria_nome UNIQUE (nome)
    );

CREATE TABLE IF NOT EXISTS livros (
                                      id           INT AUTO_INCREMENT PRIMARY KEY,
                                      titulo       VARCHAR(200)  NOT NULL,
    autor        VARCHAR(150)  NOT NULL,
    isbn         VARCHAR(20)   NOT NULL,
    editora      VARCHAR(100),
    ano_pub      INT,
    categoria_id INT,
    status       VARCHAR(20)   NOT NULL DEFAULT 'DISPONIVEL',
    criado_em    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_livro_isbn    UNIQUE (isbn),
    CONSTRAINT fk_livro_cat     FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT chk_livro_status CHECK (status IN ('DISPONIVEL','EMPRESTADO','RESERVADO','MANUTENCAO'))
    );

CREATE TABLE IF NOT EXISTS usuarios (
                                        id          INT AUTO_INCREMENT PRIMARY KEY,
                                        nome        VARCHAR(150)  NOT NULL,
    email       VARCHAR(150)  NOT NULL,
    telefone    VARCHAR(20),
    ativo       BOOLEAN       NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_usuario_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS emprestimos (
                                           id              INT AUTO_INCREMENT PRIMARY KEY,
                                           livro_id        INT           NOT NULL,
                                           usuario_id      INT           NOT NULL,
                                           data_emprestimo DATE          NOT NULL,
                                           data_prevista   DATE          NOT NULL,
                                           data_devolucao  DATE,
                                           status          VARCHAR(20)   NOT NULL DEFAULT 'ATIVO',
    observacao      VARCHAR(255),
    criado_em       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_emp_livro    FOREIGN KEY (livro_id)   REFERENCES livros(id),
    CONSTRAINT fk_emp_usuario  FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT chk_emp_status  CHECK (status IN ('ATIVO','DEVOLVIDO','ATRASADO'))
    );

-- ======================== ÍNDICES ============================

CREATE INDEX IF NOT EXISTS idx_livros_autor   ON livros(autor);
CREATE INDEX IF NOT EXISTS idx_livros_status  ON livros(status);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_emp_usuario    ON emprestimos(usuario_id);
CREATE INDEX IF NOT EXISTS idx_emp_livro      ON emprestimos(livro_id);
CREATE INDEX IF NOT EXISTS idx_emp_status     ON emprestimos(status);

-- ======================== SEEDS ==============================

INSERT INTO categorias (nome, descricao) VALUES
                                             ('Tecnologia',      'Livros sobre programação, redes e sistemas'),
                                             ('Engenharia',      'Livros de engenharia e matemática aplicada'),
                                             ('Gestão',          'Livros de gestão, negócios e empreendedorismo'),
                                             ('Design Patterns', 'Livros sobre padrões de projeto de software');

INSERT INTO livros (titulo, autor, isbn, editora, ano_pub, categoria_id, status) VALUES
                                                                                     ('Design Patterns: GoF',
                                                                                      'Gang of Four', '978-0201633610', 'Addison-Wesley', 1994, 4, 'DISPONIVEL'),
                                                                                     ('Clean Code',
                                                                                      'Robert C. Martin', '978-0132350884', 'Prentice Hall', 2008, 1, 'DISPONIVEL'),
                                                                                     ('Effective Java',
                                                                                      'Joshua Bloch', '978-0134685991', 'Addison-Wesley', 2018, 1, 'DISPONIVEL'),
                                                                                     ('Java: Como Programar',
                                                                                      'Deitel & Deitel', '978-8576059554', 'Pearson', 2017, 1, 'DISPONIVEL'),
                                                                                     ('Domain-Driven Design',
                                                                                      'Eric Evans', '978-0321125217', 'Addison-Wesley', 2003, 4, 'DISPONIVEL');

INSERT INTO usuarios (nome, email, telefone, ativo) VALUES
                                                        ('Ana Costa',    'ana.costa@fiap.com.br',    '(11)99999-0001', TRUE),
                                                        ('Bruno Silva',  'bruno.silva@fiap.com.br',  '(11)99999-0002', TRUE),
                                                        ('Carla Mendes', 'carla.mendes@fiap.com.br', '(11)99999-0003', TRUE);
