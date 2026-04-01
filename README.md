# ADC Project

Projeto individual de ADC desenvolvido em `Java 21`, com `Jersey` para os endpoints REST, `Maven` para build e `Google Firestore em Datastore mode` para persistencia.

## Visao geral

O projeto implementa um backend REST para gestao de contas de utilizador, autenticacao por token e controlo de acesso baseado em roles.

As operacoes implementadas sao:

- `Op1` `createaccount`
- `Op2` `login`
- `Op3` `showusers`
- `Op4` `deleteaccount`
- `Op5` `modaccount`
- `Op6` `showauthsessions`
- `Op7` `showuserrole`
- `Op8` `changeuserrole`
- `Op9` `changeuserpwd`
- `Op10` `logout`

## Estrutura do projeto

- `src/main/java/pt/unl/adc/project/model`
  Classes de dados da aplicacao, incluindo entidades e objetos usados nos pedidos REST.
- `src/main/java/pt/unl/adc/project/repositories`
  Camada responsavel pelo acesso ao Datastore.
- `src/main/java/pt/unl/adc/project/resources`
  Endpoints REST e logica das operacoes.
- `src/main/webapp/WEB-INF/web.xml`
  Configuracao do Jersey com raiz REST em `/rest`.
- `app.yaml`
  Configuracao de deploy no Google App Engine.

## Tecnologias usadas

- `Java 21`
- `Maven`
- `Jersey / JAX-RS`
- `Google App Engine`
- `Google Firestore em Datastore mode`
- `jetty-runner`

## Requisitos

Antes de correr o projeto, convem ter instalado:

- `Java 21`
- `Maven`
- `Google Cloud SDK (gcloud)`

## Clonar o projeto

```powershell
git clone https://github.com/Dinis-dask/Project_adc.git
cd Project_adc
```

## Build do projeto

```powershell
mvn package
```

Este comando compila o projeto e gera o ficheiro:

- `target/adc-project.war`

## Execucao local

Para correr a aplicacao localmente:

```powershell
java -jar target/jetty-runner.jar --port 8080 target/adc-project.war
```

Depois disso, os endpoints ficam disponiveis em:

```text
http://localhost:8080/rest/<operation>
```

Exemplo:

```text
http://localhost:8080/rest/login
```

### Nota sobre a execucao local

Como a aplicacao acede ao Datastore remoto, pode ser necessario autenticar localmente as `Application Default Credentials`:

```powershell
gcloud auth application-default login
gcloud config set project reliable-byte-491214-c7
```

## Deploy remoto

Para fazer deploy para o Google App Engine:

```powershell
gcloud app deploy
```

Depois do deploy, a aplicacao fica disponivel na cloud em:

```text
https://reliable-byte-491214-c7.appspot.com/rest/<operation>
```

Exemplo:

```text
https://reliable-byte-491214-c7.appspot.com/rest/showusers
```


## Persistencia

O projeto usa duas entidades principais no Datastore:

- `User`
  Conta registada de um utilizador.
- `SessionToken`
  Sessao autenticada criada apos um login com sucesso.


## Testes

As operacoes REST podem ser testadas com `Postman`, tanto em local como apos deploy remoto.

Formato base dos endpoints:

```text
/rest/<operation>
```

## Uso de IA como apoio

Durante o desenvolvimento deste projeto foram utilizadas ferramentas de apoio baseadas em IA para esclarecimento de duvidas, organizacao do trabalho e preparacao de alguns cenarios de teste. A implementacao final e os testes executados foram sempre revistos manualmente.

## Repositorio GitHub

Repositorio usado para a implementacao:

```text
https://github.com/Dinis-dask/Project_adc.git
```
