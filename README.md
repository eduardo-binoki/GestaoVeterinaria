README GITHUB: 
Sistema de Gestão Veterinária 
Sistema web desenvolvido para auxiliar no gerenciamento de clínicas veterinárias, 
centralizando o cadastro de tutores, pacientes, veterinários e o controle das principais 
atividades clínicas. 
O projeto tem como objetivo facilitar a organização dos atendimentos, o 
acompanhamento da saúde dos animais e a administração das informações da clínica. 
Tecnologias utilizadas 
Backend 
Java 
Spring Boot 
Spring Data JPA 
Spring Security 
Spring Web 
JWT (JSON Web Token) 
Bean Validation 
Maven 
Frontend 
HTML5 
CSS3 
JavaScript (Vanilla JS) 
Fetch API 
Banco de dados 
H2 Database 
Hibernate / JPA 
Funcionalidades 
Gestão de cadastros 
Cadastro e gerenciamento de tutores. 
Cadastro e gerenciamento de animais. 
Cadastro e gerenciamento de veterinários. 
Cadastro e gerenciamento de usuários. 
Controle de ativação e desativação de registros. 
Gestão de atendimentos 
Gerenciamento de consultas. 
Agendamento e controle de horários. 
Controle de status das consultas. 
Validação de conflitos de horários. 
Gerenciamento de prontuários veterinários. 
Histórico clínico dos pacientes. 
Controle de vacinação 
Cadastro de vacinas. 
Registro das aplicações. 
Controle de próximas doses. 
Consulta do histórico de vacinação. 
Controle de estoque 
Gerenciamento de produtos. 
Cadastro de categorias. 
Controle de estoque mínimo. 
Registro de entradas e saídas. 
Histórico de movimentações. 
Validação de disponibilidade de produtos. 
Segurança e autenticação 
Autenticação utilizando JWT. 
Criptografia de senhas. 
Controle de acesso por perfil. 
Proteção de endpoints. 
Validação de dados. 
Tratamento global de exceções. 
Arquitetura 
O backend utiliza uma arquitetura em camadas, separando as responsabilidades da 
aplicação: 
Controller: recebe e responde às requisições HTTP. 
Service: concentra as regras de negócio. 
Repository: realiza o acesso aos dados. 
Domain: contém as entidades e modelos do sistema. 
DTO: organiza os dados de entrada e saída da API. 
Config: concentra as configurações da aplicação. 
Exception: centraliza o tratamento de exceções. 
O frontend utiliza JavaScript puro para consumir os endpoints REST disponibilizados 
pelo backend por meio da Fetch API. 
Autenticação 
A aplicação utiliza autenticação baseada em JWT (JSON Web Token). 
Após realizar o login, o token é utilizado para autenticar as requisições aos endpoints 
protegidos da API. 
O sistema também possui controle de acesso baseado em perfis de usuário. 
Objetivos do projeto 
Este projeto foi desenvolvido com o objetivo de aplicar conhecimentos em 
desenvolvimento web, construção de APIs REST, persistência de dados, autenticação e 
autorização, integração entre frontend e backend e organização de aplicações 
utilizando Spring Boot. 
Também busca simular funcionalidades presentes em sistemas reais de gestão 
veterinária. 
Desenvolvimento 
Projeto desenvolvido para fins de aprendizado e aprimoramento de conhecimentos em 
desenvolvimento Full Stack. 
Commit message: 
docs: atualizar README do projeto 
Extended description: 
Adiciona a documentação do sistema de gestão veterinária, apresentando as 
tecnologias utilizadas, funcionalidades, arquitetura e autenticação. 
