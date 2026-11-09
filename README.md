# 🐾 Sistema de Gestão Veterinária

Sistema web desenvolvido para auxiliar no gerenciamento de clínicas veterinárias, centralizando o cadastro de tutores, pacientes, veterinários e o controle das principais atividades clínicas.

O projeto tem como objetivo facilitar a organização dos atendimentos, o acompanhamento da saúde dos animais e a administração das informações da clínica.

## 🚀 Tecnologias utilizadas

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Web
- JWT (JSON Web Token)
- Bean Validation
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)
- Fetch API

### Banco de dados
- MySQL 8
- Hibernate / JPA

## 📋 Funcionalidades

### Gestão de cadastros
- Cadastro e gerenciamento de tutores.
- Cadastro e gerenciamento de animais.
- Cadastro e gerenciamento de veterinários.
- Cadastro e gerenciamento de usuários.
- Controle de ativação e desativação de registros.

### Gestão de atendimentos
- Gerenciamento de consultas.
- Agendamento e controle de horários.
- Controle de status das consultas.
- Validação de conflitos de horários.
- Gerenciamento de prontuários veterinários.
- Histórico clínico dos pacientes.

### Controle de vacinação
- Cadastro de vacinas.
- Registro das aplicações.
- Controle de próximas doses.
- Consulta do histórico de vacinação.

### Controle de estoque
- Gerenciamento de produtos.
- Cadastro de categorias.
- Controle de estoque mínimo.
- Registro de entradas e saídas.
- Histórico de movimentações.
- Validação de disponibilidade de produtos.

### Segurança e autenticação
- Autenticação utilizando JWT.
- Criptografia de senhas.
- Controle de acesso por perfil.
- Proteção de endpoints.
- Validação de dados.
- Tratamento global de exceções.

## 🏗️ Arquitetura

O backend utiliza uma arquitetura em camadas, separando as responsabilidades da aplicação:
- **Controller:** recebe e responde às requisições HTTP.
- **Service:** concentra as regras de negócio.
- **Repository:** realiza o acesso aos dados.
- **Domain:** contém as entidades e modelos do sistema.
- **DTO:** organiza os dados de entrada e saída da API.
- **Config:** concentra as configurações da aplicação.
- **Exception:** centraliza o tratamento de exceções.

O frontend utiliza JavaScript puro para consumir os endpoints REST disponibilizados pelo backend por meio da Fetch API.

## 🔐 Autenticação

A aplicação utiliza autenticação baseada em JWT (JSON Web Token).
Após realizar o login, o token é utilizado para autenticar as requisições aos endpoints protegidos da API.
O sistema também possui controle de acesso baseado em perfis de usuário.

## 🎯 Objetivos do projeto

Este projeto foi desenvolvido com o objetivo de aplicar conhecimentos em desenvolvimento web, construção de APIs REST, persistência de dados, autenticação e autorização, integração entre frontend e backend e organização de aplicações utilizando Spring Boot.

Também busca simular funcionalidades presentes em sistemas reais de gestão veterinária.

## 💻 Desenvolvimento

Projeto desenvolvido para fins de aprendizado e aprimoramento de conhecimentos em desenvolvimento Full Stack.

## ⚙️ Como executar

O projeto está configurado para usar **MySQL** por padrão (perfil `mysql`).

### Pré-requisitos

- **JDK 17 ou superior** (verifique com `java -version`)
- **Docker** (para subir o MySQL via Docker Compose) ou um MySQL 8 local
- Não é necessário instalar Maven: use o `./mvnw` incluso no projeto

### 1. Subir o banco de dados (MySQL)

A forma mais simples é com Docker Compose, que já cria o banco `sistema_vet_app`, o usuário `vet`/`vet` e importa o dump `sistema_vet_app.sql` automaticamente:

```bash
docker compose up -d
```

- MySQL: `localhost:3306`
- Banco: `sistema_vet_app`
- Usuário: `vet`
- Senha: `vet`
- phpMyAdmin: http://localhost:8081 (login `vet` / `vet`)

> Se preferir um MySQL instalado localmente, crie o banco `sistema_vet_app` e o usuário `vet` com senha `vet`, e importe o arquivo `sistema_vet_app.sql` (`mysql -uvet -p sistema_vet_app < sistema_vet_app.sql`).

### 2. Rodar o backend

Na pasta `Sistema-Veterinario/Sistema-Veterinario`:

```bash
./mvnw spring-boot:run
```

A API sobe em http://localhost:8080.

### 3. Abrir o frontend

O frontend são arquivos estáticos na pasta `Sistema-Veterinario-Frontend`. Sirva a pasta com qualquer servidor estático, por exemplo:

```bash
python3 -m http.server 5500 --directory Sistema-Veterinario-Frontend
```

Acesse http://localhost:5500.

### Credenciais de acesso

- **Admin:** `admin@vet.com` / `admin123`
- **Veterinário:** `vet@vet.com` / `vet123`
- **Recepção:** `recepcao@vet.com` / `recepcao123`

> Observação: os dados padrão só são criados automaticamente quando o banco está vazio. Ao importar o dump, os usuários já vêm cadastrados.
