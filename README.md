# 📋 Sistema de Board Kanban

> Um sistema completo de gerenciamento de tarefas tipo Kanban desenvolvido em Java com persistência MySQL.

## 📖 Sobre o Projeto

Este é um sistema de board para gerenciamento de tarefas que permite organizar o fluxo de trabalho através de colunas personalizáveis. Desenvolvido como projeto do Bootcamp DIO, implementa todas as funcionalidades essenciais de um board Kanban com recursos avançados de relatórios e rastreamento.

## 🔧 O que o Sistema Faz

### Gerenciamento de Boards
- Criação de boards personalizados com nomes únicos
- Configuração de colunas obrigatórias (inicial, final, cancelamento)
- Adição de colunas pendentes intermediárias conforme necessário
- Visualização e exclusão de boards existentes

### Gestão de Cards (Tarefas)
- Criação de cards com título e descrição
- Movimentação sequencial entre colunas respeitando o fluxo
- Sistema de bloqueio/desbloqueio com justificativas obrigatórias
- Cancelamento de cards (movimentação direta para coluna de cancelamento)
- Visualização detalhada de cards individuais

### Sistema de Relatórios
- **Relatório de Tempo**: Mostra quanto tempo cada tarefa ficou em cada coluna e o tempo total de conclusão
- **Relatório de Bloqueios**: Exibe histórico completo de bloqueios com motivos, durações e desbloqueios

### Regras de Negócio
- Cards só podem avançar sequencialmente entre colunas
- Cards bloqueados não podem ser movidos até serem desbloqueados
- Cards finalizados não podem mais ser movimentados
- Cada board deve ter exatamente uma coluna de cada tipo obrigatório
- Motivos são obrigatórios para bloqueios e desbloqueios

## 🛠️ Tecnologias Utilizadas

- **Java 17** - Linguagem principal
- **MySQL** - Banco de dados relacional
- **Liquibase** - Controle de versão do banco de dados
- **Lombok** - Redução de boilerplate code
- **Gradle** - Gerenciamento de dependências e build

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definidas:

`
📁 Estrutura do Projeto
├── 🎛️ UI (Interface)     → Menus interativos no console
├── ⚙️ Service (Negócio)  → Lógica de negócio e validações
├── 💾 DAO (Dados)        → Acesso ao banco de dados
├── 🏛️ Entity (Modelo)    → Representação das entidades
├── 📦 DTO (Transporte)   → Objetos de transferência
└── ⚠️ Exception (Erros)  → Tratamento de exceções
`

### Banco de Dados

O sistema utiliza 5 tabelas principais:

- **BOARDS** - Armazena informações dos boards
- **BOARDS_COLUMNS** - Define as colunas de cada board
- **CARDS** - Contém os cards/tarefas
- **BLOCKS** - Registra histórico de bloqueios
- **CARD_MOVEMENTS** - Rastreia movimentação entre colunas

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- MySQL instalado e configurado

### Configuração
`ash
# 1. Configure o banco de dados
mysql -u root -p < setup-database.sql

# 2. Execute a aplicação
.\run.bat          # Windows
./gradlew run      # Linux/Mac
`

### Primeiro Uso
1. Execute o programa
2. Escolha "Criar um novo board"
3. Defina nome e colunas do board
4. Selecione o board criado para começar a usar

## 💡 Funcionalidades em Detalhes

### Interface do Sistema
O sistema apresenta dois menus principais:

**Menu Principal:**
- Criar novo board
- Selecionar board existente  
- Excluir board
- Sair do sistema

**Menu do Board:**
- Criar card
- Mover card para próxima coluna
- Bloquear/desbloquear card
- Cancelar card
- Visualizar board, colunas e cards
- Gerar relatórios de tempo e bloqueios

### Sistema de Colunas
Cada board possui uma estrutura de colunas ordenada:
1. **Coluna Inicial** - Onde novos cards são criados
2. **Colunas Pendentes** - Etapas intermediárias (quantidade variável)
3. **Coluna Final** - Para tarefas concluídas
4. **Coluna de Cancelamento** - Para tarefas canceladas

### Rastreamento e Histórico
O sistema mantém registro completo de:
- Data/hora de criação de cada card
- Histórico de movimentação entre colunas
- Períodos de bloqueio com motivos
- Tempo gasto em cada etapa do processo

---

Este projeto representa uma implementação completa de um sistema de gerenciamento de tarefas estilo Kanban, desenvolvido em Java com foco em boas práticas de programação, arquitetura limpa e persistência de dados. O sistema oferece todas as funcionalidades essenciais para organização de fluxo de trabalho, incluindo recursos avançados como relatórios detalhados de tempo e bloqueios, sistema de rastreamento de movimentações e interface intuitiva via console. Através de uma arquitetura bem estruturada em camadas (UI, Service, DAO, Entity), o projeto demonstra o uso efetivo de tecnologias modernas como Liquibase para versionamento de banco de dados, Lombok para redução de código repetitivo, e padrões de design que garantem escalabilidade e manutenibilidade do código.

**Bootcamp DIO** - Projeto desenvolvido para demonstrar conhecimentos em Java e desenvolvimento de sistemas
