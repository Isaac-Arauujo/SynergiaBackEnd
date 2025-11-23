
CREATE DATABASE IF NOT EXISTS synergia_blog;
USE synergia_blog;

CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_completo VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    foto_perfil VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_cpf (cpf),
    INDEX idx_data_nascimento (data_nascimento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE locais (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    imagem_url VARCHAR(255) NOT NULL,
    rua VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    data_inicio DATE NOT NULL,
    data_final DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  
    INDEX idx_data_inicio (data_inicio),
    INDEX idx_data_final (data_final),
    INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE ferramentas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    imagem_url VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_nome (nome),
    INDEX idx_quantidade (quantidade)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE inscricoes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    local_id BIGINT NOT NULL,
    data_desejada DATE NOT NULL,
    status ENUM('PENDENTE', 'CONFIRMADA', 'RECUSADA') DEFAULT 'PENDENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (local_id) REFERENCES locais(id) ON DELETE CASCADE,
    
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_local_id (local_id),
    INDEX idx_data_desejada (data_desejada),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    
    -- Garantir que um usuário não se inscreva duas vezes no mesmo local
    UNIQUE KEY unique_usuario_local (usuario_id, local_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- TABELA: local_ferramenta
-- Descrição: Tabela de relacionamento entre locais e ferramentas (N:N)
-- =============================================
CREATE TABLE local_ferramenta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    local_id BIGINT NOT NULL,
    ferramenta_id BIGINT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    
    -- Chaves estrangeiras
    FOREIGN KEY (local_id) REFERENCES locais(id) ON DELETE CASCADE,
    FOREIGN KEY (ferramenta_id) REFERENCES ferramentas(id) ON DELETE CASCADE,
    
    -- Índices
    INDEX idx_local_id (local_id),
    INDEX idx_ferramenta_id (ferramenta_id),
    
    -- Garantir que não haja duplicação de ferramentas no mesmo local
    UNIQUE KEY unique_local_ferramenta (local_id, ferramenta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- INSERÇÃO DE DADOS INICIAIS
-- =============================================

-- Inserir usuários de exemplo
INSERT INTO usuarios (nome_completo, data_nascimento, cpf, email, senha, foto_perfil) VALUES
('Administrador Sistema', '1990-01-15', '12345678901', 'admin@synergia.org', 'senha123', 'https://example.com/admin.jpg'),
('João Silva', '1995-05-20', '23456789012', 'joao.silva@email.com', 'senha123', 'https://example.com/joao.jpg'),
('Maria Santos', '1988-08-12', '34567890123', 'maria.santos@email.com', 'senha123', 'https://example.com/maria.jpg'),
('Pedro Oliveira', '2000-03-30', '45678901234', 'pedro.oliveira@email.com', 'senha123', 'https://example.com/pedro.jpg'),
('Ana Costa', '1992-11-05', '56789012345', 'ana.costa@email.com', 'senha123', 'https://example.com/ana.jpg');

-- Inserir ferramentas de exemplo
INSERT INTO ferramentas (nome, descricao, imagem_url, quantidade) VALUES
('Martelo', 'Martelo de unha 500g, cabo de madeira', 'https://example.com/martelo.jpg', 15),
('Serra Tico-Tico', 'Serra elétrica para cortes precisos', 'https://example.com/serra.jpg', 8),
('Furadeira', 'Furadeira sem fio 18V com 2 baterias', 'https://example.com/furadeira.jpg', 12),
('Escada', 'Escada extensível alumínio 4m', 'https://example.com/escada.jpg', 6),
('Trena', 'Trena métrica 8m automática', 'https://example.com/trena.jpg', 20),
('Pá', 'Pá de jardinagem cabo longo', 'https://example.com/pa.jpg', 10),
('Carrinho de Mão', 'Carrinho de obra capacidade 100L', 'https://example.com/carrinho.jpg', 5),
('Luvas', 'Luvas de proteção tamanho M', 'https://example.com/luvas.jpg', 30),
('Capacete', 'Capacete de segurança regulável', 'https://example.com/capacete.jpg', 25),
('Cordas', 'Cordas de nylon 10m resistentes', 'https://example.com/cordas.jpg', 15);

-- Inserir locais de exemplo
INSERT INTO locais (nome, descricao, imagem_url, rua, numero, cep, data_inicio, data_final) VALUES
('Parque Central', 'Reforma do playground infantil do parque central da cidade. Precisamos de voluntários para pintura, reparos e limpeza.', 'https://example.com/parque-central.jpg', 'Rua das Flores', '100', '12345678', '2024-03-01', '2024-03-15'),
('Creche Esperança', 'Manutenção do telhado e pintura das salas da creche comunitária. Ideal para quem tem experiência com construção.', 'https://example.com/creche-esperanca.jpg', 'Avenida Brasil', '250', '23456789', '2024-03-10', '2024-03-25'),
('Asilo São Francisco', 'Jardim terapêutico para idosos. Plantio de flores, construção de canteiros e bancos.', 'https://example.com/asilo-sf.jpg', 'Rua Paz', '50', '34567890', '2024-03-05', '2024-03-20'),
('Praça da Juventude', 'Instalação de equipamentos de ginástica ao ar livre e bancos novos.', 'https://example.com/praca-juventude.jpg', 'Rua Esportes', '300', '45678901', '2024-04-01', '2024-04-30'),
('Biblioteca Comunitária', 'Organização e catalogação de livros, reparo em estantes e criação de espaço de leitura.', 'https://example.com/biblioteca.jpg', 'Rua Conhecimento', '75', '56789012', '2024-03-15', '2024-04-10');

-- Inserir relacionamentos locais-ferramentas
INSERT INTO local_ferramenta (local_id, ferramenta_id, quantidade) VALUES
-- Parque Central
(1, 1, 5),  -- Martelos
(1, 3, 3),  -- Furadeiras
(1, 5, 8),  -- Trenas
(1, 7, 2),  -- Carrinhos de mão
(1, 8, 10), -- Luvas
(1, 9, 8),  -- Capacetes

-- Creche Esperança
(2, 1, 3),  -- Martelos
(2, 2, 2),  -- Serras
(2, 3, 4),  -- Furadeiras
(2, 4, 2),  -- Escadas
(2, 8, 8),  -- Luvas
(2, 9, 6),  -- Capacetes

-- Asilo São Francisco
(3, 6, 4),  -- Pás
(3, 7, 1),  -- Carrinho de mão
(3, 8, 6),  -- Luvas
(3, 10, 3), -- Cordas

-- Praça da Juventude
(4, 1, 4),  -- Martelos
(4, 3, 5),  -- Furadeiras
(4, 4, 3),  -- Escadas
(4, 5, 6),  -- Trenas
(4, 8, 12), -- Luvas
(4, 9, 10), -- Capacetes

-- Biblioteca Comunitária
(5, 1, 2),  -- Martelos
(5, 3, 2),  -- Furadeiras
(5, 5, 4);  -- Trenas

-- Inserir inscrições de exemplo
INSERT INTO inscricoes (usuario_id, local_id, data_desejada, status) VALUES
-- Inscrições pendentes
(2, 1, '2024-03-05', 'PENDENTE'),
(3, 1, '2024-03-08', 'PENDENTE'),
(4, 2, '2024-03-12', 'PENDENTE'),
(5, 3, '2024-03-10', 'PENDENTE'),

-- Inscrições confirmadas
(2, 3, '2024-03-15', 'CONFIRMADA'),
(3, 2, '2024-03-20', 'CONFIRMADA'),
(4, 1, '2024-03-03', 'CONFIRMADA'),

-- Inscrições recusadas
(5, 4, '2024-04-05', 'RECUSADA');

-- =============================================
-- VIEWS PARA CONSULTAS COMUNS
-- =============================================

-- View: Inscrições com detalhes completos
CREATE VIEW vw_inscricoes_detalhadas AS
SELECT 
    i.id,
    i.data_desejada,
    i.status,
    i.created_at,
    u.id as usuario_id,
    u.nome_completo as usuario_nome,
    u.email as usuario_email,
    u.data_nascimento as usuario_data_nascimento,
    u.foto_perfil as usuario_foto,
    l.id as local_id,
    l.nome as local_nome,
    l.descricao as local_descricao,
    l.imagem_url as local_imagem,
    l.data_inicio as local_data_inicio,
    l.data_final as local_data_final
FROM inscricoes i
INNER JOIN usuarios u ON i.usuario_id = u.id
INNER JOIN locais l ON i.local_id = l.id;

-- View: Locais com contagem de ferramentas
CREATE VIEW vw_locais_ferramentas AS
SELECT 
    l.*,
    COUNT(lf.id) as total_ferramentas,
    SUM(lf.quantidade) as total_itens_alocados
FROM locais l
LEFT JOIN local_ferramenta lf ON l.id = lf.local_id
GROUP BY l.id;

-- View: Ferramentas com disponibilidade
CREATE VIEW vw_ferramentas_disponibilidade AS
SELECT 
    f.*,
    COALESCE(SUM(lf.quantidade), 0) as quantidade_alocada,
    (f.quantidade - COALESCE(SUM(lf.quantidade), 0)) as quantidade_disponivel
FROM ferramentas f
LEFT JOIN local_ferramenta lf ON f.id = lf.ferramenta_id
GROUP BY f.id;

-- View: Estatísticas de usuários
CREATE VIEW vw_estatisticas_usuarios AS
SELECT 
    u.id,
    u.nome_completo,
    u.email,
    COUNT(i.id) as total_inscricoes,
    SUM(CASE WHEN i.status = 'CONFIRMADA' THEN 1 ELSE 0 END) as inscricoes_confirmadas,
    SUM(CASE WHEN i.status = 'PENDENTE' THEN 1 ELSE 0 END) as inscricoes_pendentes,
    SUM(CASE WHEN i.status = 'RECUSADA' THEN 1 ELSE 0 END) as inscricoes_recusadas
FROM usuarios u
LEFT JOIN inscricoes i ON u.id = i.usuario_id
GROUP BY u.id, u.nome_completo, u.email;

-- =============================================
-- STORED PROCEDURES
-- =============================================

-- Procedure: Verificar disponibilidade de local na data
DELIMITER //
CREATE PROCEDURE sp_verificar_disponibilidade_local(
    IN p_local_id BIGINT,
    IN p_data DATE,
    OUT p_disponivel BOOLEAN
)
BEGIN
    DECLARE data_valida BOOLEAN;
    DECLARE existe_inscricao BOOLEAN;
    
    -- Verificar se a data está dentro do período do local
    SELECT EXISTS(
        SELECT 1 FROM locais 
        WHERE id = p_local_id 
        AND p_data BETWEEN data_inicio AND data_final
    ) INTO data_valida;
    
    -- Verificar se já existe inscrição nessa data
    SELECT EXISTS(
        SELECT 1 FROM inscricoes 
        WHERE local_id = p_local_id 
        AND data_desejada = p_data
    ) INTO existe_inscricao;
    
    SET p_disponivel = (data_valida AND NOT existe_inscricao);
END //
DELIMITER ;

-- Procedure: Obter estatísticas do sistema
DELIMITER //
CREATE PROCEDURE sp_obter_estatisticas_sistema()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM usuarios) as total_usuarios,
        (SELECT COUNT(*) FROM locais) as total_locais,
        (SELECT COUNT(*) FROM ferramentas) as total_ferramentas,
        (SELECT COUNT(*) FROM inscricoes) as total_inscricoes,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'PENDENTE') as inscricoes_pendentes,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'CONFIRMADA') as inscricoes_confirmadas,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'RECUSADA') as inscricoes_recusadas;
END //
DELIMITER ;

-- =============================================
-- TRIGGERS
-- =============================================

-- Trigger: Validar quantidade de ferramentas antes de inserir no local
DELIMITER //
CREATE TRIGGER tg_validar_quantidade_ferramenta
BEFORE INSERT ON local_ferramenta
FOR EACH ROW
BEGIN
    DECLARE quantidade_total INT;
    DECLARE quantidade_alocada INT;
    
    -- Obter quantidade total da ferramenta
    SELECT quantidade INTO quantidade_total 
    FROM ferramentas 
    WHERE id = NEW.ferramenta_id;
    
    -- Obter quantidade já alocada
    SELECT COALESCE(SUM(quantidade), 0) INTO quantidade_alocada
    FROM local_ferramenta 
    WHERE ferramenta_id = NEW.ferramenta_id;
    
    -- Verificar se há quantidade suficiente
    IF (quantidade_alocada + NEW.quantidade) > quantidade_total THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Quantidade insuficiente da ferramenta disponível';
    END IF;
END //
DELIMITER ;

-- Trigger: Log de alterações de status de inscrição
CREATE TABLE inscricoes_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inscricao_id BIGINT NOT NULL,
    status_anterior ENUM('PENDENTE', 'CONFIRMADA', 'RECUSADA'),
    status_novo ENUM('PENDENTE', 'CONFIRMADA', 'RECUSADA'),
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_alteracao VARCHAR(100)
);

DELIMITER //
CREATE TRIGGER tg_log_alteracao_status
AFTER UPDATE ON inscricoes
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO inscricoes_log (inscricao_id, status_anterior, status_novo, usuario_alteracao)
        VALUES (NEW.id, OLD.status, NEW.status, USER());
    END IF;
END //
DELIMITER ;

-- =============================================
-- CONSULTAS ÚTEIS
-- =============================================

-- Consulta: Usuários com suas inscrições
SELECT 
    u.nome_completo,
    u.email,
    l.nome as local_inscrito,
    i.data_desejada,
    i.status
FROM usuarios u
LEFT JOIN inscricoes i ON u.id = i.usuario_id
LEFT JOIN locais l ON i.local_id = l.id
ORDER BY u.nome_completo;

-- Consulta: Ferramentas por local
SELECT 
    l.nome as local,
    f.nome as ferramenta,
    lf.quantidade,
    f.quantidade as estoque_total
FROM locais l
JOIN local_ferramenta lf ON l.id = lf.local_id
JOIN ferramentas f ON lf.ferramenta_id = f.id
ORDER BY l.nome, f.nome;

-- Consulta: Disponibilidade de locais por data
SELECT 
    l.nome,
    l.data_inicio,
    l.data_final,
    CASE 
        WHEN '2024-03-12' BETWEEN l.data_inicio AND l.data_final 
        AND NOT EXISTS (
            SELECT 1 FROM inscricoes i 
            WHERE i.local_id = l.id 
            AND i.data_desejada = '2024-03-12'
        ) THEN 'DISPONÍVEL'
        ELSE 'INDISPONÍVEL'
    END as status_data
FROM locais l;


# No MySQL, verifique o estado atual:
SELECT id, status, usuario_id, local_id FROM inscricoes WHERE id = 10;

SELECT id, nome, descricao, quantidade, imagem_url 
FROM ferramentas 
ORDER BY id;




-- Adicionar coluna is_admin na tabela usuarios
ALTER TABLE usuarios ADD COLUMN is_admin BOOLEAN DEFAULT FALSE NOT NULL;

-- Atualizar usuário admin existente
UPDATE usuarios SET is_admin = TRUE WHERE email = 'admin@synergia.org';

-- Inserir mais um admin de exemplo
INSERT INTO usuarios (nome_completo, data_nascimento, cpf, email, senha, is_admin) VALUES
('Super Admin', '1985-01-01', '99988877766', 'superadmin@synergia.org', 'senha123', TRUE);

-- Atualizar a view vw_estatisticas_usuarios para incluir is_admin
DROP VIEW IF EXISTS vw_estatisticas_usuarios;

CREATE VIEW vw_estatisticas_usuarios AS
SELECT 
    u.id,
    u.nome_completo,
    u.email,
    u.is_admin,
    COUNT(i.id) as total_inscricoes,
    SUM(CASE WHEN i.status = 'CONFIRMADA' THEN 1 ELSE 0 END) as inscricoes_confirmadas,
    SUM(CASE WHEN i.status = 'PENDENTE' THEN 1 ELSE 0 END) as inscricoes_pendentes,
    SUM(CASE WHEN i.status = 'RECUSADA' THEN 1 ELSE 0 END) as inscricoes_recusadas
FROM usuarios u
LEFT JOIN inscricoes i ON u.id = i.usuario_id
GROUP BY u.id, u.nome_completo, u.email, u.is_admin;

-- Procedure para obter estatísticas do sistema
DELIMITER //
CREATE PROCEDURE sp_obter_estatisticas_admin()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM usuarios WHERE is_admin = TRUE) as total_admins,
        (SELECT COUNT(*) FROM usuarios WHERE is_admin = FALSE) as total_voluntarios,
        (SELECT COUNT(*) FROM locais) as total_locais,
        (SELECT COUNT(*) FROM ferramentas) as total_ferramentas,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'PENDENTE') as inscricoes_pendentes,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'CONFIRMADA') as inscricoes_confirmadas,
        (SELECT COUNT(*) FROM inscricoes WHERE status = 'RECUSADA') as inscricoes_recusadas;
END //
DELIMITER ;
-- =============================================
-- FIM DO SCRIPT
-- =============================================