# Liga BJJ - API
API Desenvolvida para consumo do [Portal: Liga BJJ](https://liga-bjj.vercel.app), aplicando middleare entre o banco de dados e camadas de segurança com Authentication Server Oauth2 + Cryptografia ponta-a-ponta para métodos de access e refresh token.

## API Rest com autenticação Oauth2 e documentação usando Swagger

Sobre:
 - Controle de acesso baseado em regras com JWT
 - Tecnologias utilizadas
    - Java 8
    - Spring Boot 2.7.0
    - Spring JPA latest
    - Spring Security 5.6.4     
    - Spring Secutiry Oauth2 Autoconfigure 2.1.5
    - Springfox (Swagger) 3.0.0
    - H2 Database - Base de teste
    - Lombok - [Ajuda para configurar o lombok](https://projectlombok.org/setup/eclipse)
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
    git clone https://github.com/andresinho20049/liga-bjj-back.git
 ```

2. cd folder
```cmd
  cd ./liga-bjj-back
```

3. Build Project
```mvn
    mvn clean package
```

4. Start Project
```mvn
  mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=5000
```

5. Open Swagger
  - No navegador digite a url: http://localhost:5000/api/swagger-ui/index.html
  - ou se preferir [clique aqui](http://localhost:5000/api/swagger-ui/index.html)

### Config Lombok

> Neste exemplo vou considerar o uso do Eclipse, mas basta adaptar ao seu uso, ok.

1. Este projeto usa o lombok, se você ainda não instalou este plugin no seu eclipse, clique em:
Ajuda > Instalar novo software...
No campo 'Work with', cole: https://projectlombok.org/p2
Marque lombok e depois click em finish

2. Etapa opcional: Click Project > Update maven project > Ok

## Modelos
### User
```json
{
  "id": 8,
  "name": "black",
  "email": "black@email.com",
  "belt": "Black",
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
  "belt": "Black",
  "user_name": "black@email.com",
  "scope": [
    "all"
  ],
  "name": "black",
  "exp": 1677036698,
  "authorities": [
    "ROLE_ADMIN"
  ],
  "jti": "c76f2a5c-3425-46b1-8a24-b962b4ed1d51",
  "client_id": "***"
}
```

## Cloud Native - App Engine Standard Google Cloud
Conversão projeto Spring Boot para App Engine Standard.

Esta amostra demonstra como implantar um aplicativo Java 8 Spring Boot no Google App Engine. O tempo de execução do Java 8 App Engine espera que um [arquivo WAR seja carregado](https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp).

### Configurar
No pom.xml, atualize o [plug-in Maven do App Engine](https://cloud.google.com/appengine/docs/standard/java/tools/maven-reference) com seu ID de projeto do Google Cloud:
```pom
  <plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>appengine-maven-plugin</artifactId>
    <version>2.2.0</version>
    <configuration>
      <projectId>{myProjectId}</projectId>
      <version>GCLOUD_CONFIG</version>
    </configuration>
  </plugin>
```

**Observação**: GCLOUD_CONFIG é uma versão especial para geração automática de uma versão do App Engine. Altere este campo para especificar um nome de versão específico.

### Run
Executando localmente \
`mvn package appengine:run`

Para usar visite: http://localhost:8080/

Implantando \
`mvn package appengine:deploy`

Para usar visite: https://YOUR-PROJECT-ID.appspot.com

## Etapas para Converter um app Spring Boot para App Engine Standard
**Obs:** Use empacotador WAR

### Crie uma nova Implementação de `SpringBootServletInitializer`
Crie a classe no mesmo pacote de `SpringBootApplication` 
```Java
  public class ServletInitializer extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  return application.sources(YourApplication.class);
  }
}
```

### Remover Tomcat Starter
O `Google App Engine Standard` implanta o seu `WAR` em um `servidor Jetty`. O inicializador do `Spring Boot` inclui o `Tomcat` por padrão. Isto irá introduzir conflitos. \
Excluir dependências do Tomcat:

```
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
      <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
```
Não inclua as dependências do Jetty. Mas você deve incluir a dependência da `API do Servlet`:

```
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
  </dependency>
```

## Adicionar plug-in padrão do App Engine
No `pom.xml`, adicione o plug-in padrão do App Engine
```
  <plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>appengine-maven-plugin</artifactId>
    <version>2.2.0</version>
  </plugin>
```
Este plug-in é usado para executar o servidor de desenvolvimento local, bem como implantar o aplicativo no Google App Engine

### Adicionar configuração do App Engine
Adicione um `src/main/webapp/WEB-INF/appengine-web.xml`:

```
  <appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
    <version>1</version>
    <threadsafe>true</threadsafe>
    <runtime>java8</runtime>
  </appengine-web-app>
```
Essa configuração é necessária para aplicativos em execução no Google App Engine.

### Excluir JUL para ponte SLF4J
A ponte de log padrão do Spring Boot entra em conflito com o sistema de log do Jetty. Para poder capturar os logs de inicialização do Spring Boot, você precisa excluir a `org.slf4j:jul-to-slf4j` dependência. A maneira mais fácil de fazer isso é definir o escopo da dependência como `provided`, para que não seja incluído no `WAR` arquivo:

```
  <!-- Exclude any jul-to-slf4j -->
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jul-to-slf4j</artifactId>
    <scope>provided</scope>
  </dependency>
```

## Erros de falta de memória
Com `Spring Boot >= 1.5.6`, você pode encontrar erros de falta de memória na inicialização. Siga estas instruções para solucionar esse problema:

Dentro de `src/main/resources`, adicione um arquivo `logging.properties` com:
```
  .level = INFO
```
Dentro de `src/main/webapp/WEB-INF/appengine-web.xml`, adicione uma configuração que aponte para o novo arquivo `logging.properties`.
```
  <system-properties>
    <property name="java.util.logging.config.file" value="WEB-INF/classes/logging.properties"/>
  </system-properties>
```

> **Projeto:** Liga BJJ API      
> **Dev:** André Carlos [(andresinho20049)](https://github.com/andresinho20049)       
> **Url-Teste:** https://liga-bjj-back-api.up.railway.app/api
