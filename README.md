# api-company

O projeto tem como objetivo a implementação de uma api com entidades e funcionalidades de uma empresa, visando o aperfeiçoamento e prática de conceitos relacionados a **Spring MVC**, **Spring Data JPA**, **Spring Boot**, **MySQL** entre outros. Possui como norte as especificações presentes no seguinte link: [documentação](https://docs.google.com/document/d/1WxelFOK1mXEdvl31PahipRtMZuf4Z9o3W_zFi_hWkNY/edit). Porém, foram realizados ajustes e modificações em algumas funcionalidades, com o objetivo de enriquecer ainda mais o desenvolvimento, sendo gerada uma nova documentação atualizada, presente neste link: [documentação atualizada](https://docs.google.com/document/d/1kI2vQJ1MlQBep0GmrYbdQddSigbKmmCyYuTS4bPERQ0/edit?usp=sharing).

## Instalação

O projeto possui um arquivo Dockerfile e um arquivo docker-compose.yml, que podem ser utilizados para encapsular e rodar o projeto utilizando Docker. Primeiramente é necessário gerar um arquivo .jar com as classes e arquivos do projeto, que pode ser feito utilizando o comando **mvn clean install package**. Em seguida, para instalar as imagens com dependências necessárias e rodar o projeto utilizando docker, pode ser utilizado o comando a seguir:

```bash
docker-compose up
```
Esse comando analisa e instala as dependências contidas no arquivo docker-compose.yml, gerando imagens para as dependências e a imagem respectiva do projeto, em seguida, roda a aplicação.

Obs: O usuário e senha do banco de dados utilizado no projeto estão contidos no arquivo src/main/java/resoources/application.properties e precisam estar configurados ou alterados para um valor válido antes de rodar o projeto.
