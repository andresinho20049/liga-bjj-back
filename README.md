# Spring Oauth2 - Boilerplate

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/andresinho20049/spring-oauth-boilerplate/blob/master/README.md)

### API Rest com autenticação Oauth2 e documentada usando Swagger

Este projeto Spring boot foi desenvolvido com as configurações padrão de autenticação e documentação para servir de base para o desenvolvimento de outros projetos.

> Sou Brasileiro, não achou que eu não fosse subir um overview(opps: visão global) em português né. :)

Sobre:
 - Controle de acesso baseado em regras com JWT
 - Tecnologias utilizadas
    - Java 8
    - Spring Boot 2.7.0
    - Spring JPA latest
    - Spring Security 5.6.4     
    (Eu prefiro usar a implementação com WebSecurityConfigurerAdapter pois é melhor para configurar do que a nova implementação de SecurityFilterChain)
    - Spring Secutiry Oauth2 Autoconfigure 2.1.5
    - Springfox (Swagger) 3.0.0
    - H2 Database - Base de teste
    - Lombok - [Ajuda para configurar o lombok](https://projectlombok.org/setup/eclipse)
    - Log4J
    - Project Maven
- Application.properties - Padrão
    - Port: 5000
    - Profile: dev
    - Base path: /api
    - Encrypt: bcrypt
    - Hibernate DDL: update
- ExceptionHandler
    - ProjectException:
        - Status: 400
        - Description: Exceção provocada, n motivos, mas principalmente regra de negócio
        (A ideia aqui é o Dev em qualquer ponto do código fazer um throw new ProjectException(msg), que será retornado um 400 com a mensagem)
    - AuthorizationException:
        - Status: 403
        - Description: Acesso negado com RuntimeException

## Começando
1. Git clone project
 ```git
    git clone https://github.com/andresinho20049/spring-oauth-boilerplate
 ```

2. Dentro da pasta do projeto, execute
```mvn
    mvn clean install
```

> Neste exemplo vou considerar o uso do Eclipse, mas basta adaptar ao seu uso, ok.

3. Este projeto usa o lombok, se você ainda não instalou este plugin no seu eclipse, clique em:
Ajuda > Instalar novo software...
No campo 'Work with', cole: https://projectlombok.org/p2
Marque lombok e depois click em finish

4. Etapa opcional: Click Project > Update maven project > Ok

5. Pressione em start, se preferir cole este comando no seu terminal
```mvn
mvn clean spring-boot:run 
```

### Authentication
Em initialUser de ambos os perfis, está configurado para registrar um usuário/regras de teste

_O Padrão é_:     
Username: admin@email.com   
Password: password@1234

Com o projeto em execução, visite a página Swagger para testar os endpoints
  - No navegador digite o path: /swagger-ui/index.html
  - ou se preferir [clique aqui](http://localhost:5000/api/swagger-ui/index.html)


## Modelos
### User
```json
{
  "id": 1,
  "name": "Admin",
  "email": "admin@email.com",
  "password": "****"
  "updatePassword": true,
  "active": true,
  "roles": [
    {
      "name": "ROLE_ADMIN"
    }
  ]
}
```

### Role
```json
[
  {
    "name": "ROLE_ADMIN"
  },
  {
    "name": "ROLE_VIEW_USER"
  },
  {
    "name": "ROLE_CREATE_USER"
  },
  {
    "name": "ROLE_UPDATE_USER"
  },
  {
    "name": "ROLE_DISABLE_USER"
  }
]
```

### JWT Payload Exemplo
```json
{
  "aud": [
    "restservice"
  ],
  "updatePassword": true,
  "user_name": "admin@email.com",
  "scope": [
    "all"
  ],
  "name": "Admin",
  "exp": 1655404387,
  "authorities": [
    "ROLE_ADMIN"
  ],
  "jti": "7d12042b-856b-41a7-b1d9-6acb220840a7",
  "client_id": "52da334b25d96304a09901705846663fef41ce8f"
}
```

## Considerações
Desenvolvi este projeto para ter uma base, para que no futuro não perca tempo configurando autenticação, documentação (Swagger) e estrutura de pacotes.

Também quero poder compartilhar e evoluir esse projeto, atualmente trabalho como desenvolvedor pleno, e sei o quanto podemos evoluir a cada dia.


> **Projeto:** Spring Oauth2 - Boilerplate      
> **Autor:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)       
> **Resumo:** Modelo padrão para projeto Spring com Oauth2# Liga BJJ Back-end
