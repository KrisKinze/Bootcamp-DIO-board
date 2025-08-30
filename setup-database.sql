-- Script de configuração inicial do banco de dados
-- Execute este script no MySQL como root para configurar o projeto

-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS board;

-- Criar o usuário do projeto
CREATE USER IF NOT EXISTS 'board'@'localhost' IDENTIFIED BY 'board';

-- Conceder permissões
GRANT ALL PRIVILEGES ON board.* TO 'board'@'localhost';

-- Aplicar as alterações
FLUSH PRIVILEGES;

-- Selecionar o banco para uso
USE board;

-- Verificar se a configuração está correta
SELECT 'Banco de dados configurado com sucesso!' as status;
