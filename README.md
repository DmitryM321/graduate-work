Аттестационный проект курса ISA Java-разработчик.

Описание проекта:
Проект представляет собой интернет-магазин по продаже товаров через объявления.
 
Функциональность:
- Авторизация и аутентификация пользователей
- Распределение ролей между пользователями: USER и ADMIN
- Управление объявлениями: создание, удаление, редактирование
- Управление комментариями: создание, удаление, редактирование
- Пользователи могут видеть объявления других пользователей и оставлять комментарии
- Поиск объявления по названию 

В проекте используются следующие инструменты:
- Java 11
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Swagger
- Lombok
- Stream API
- Mapstruct
- PostgreSQL
- Liquibase 
- Mockito
- Docker image
  - Git
- REST 

Для запуска приложения необходимо:
1. Склонировать проект и открыть его в среде разработки.
2. Указать путь к вашей базе данных в файле application.properties.
3. Запустить Docker.
4. Скачать образ, используя команду docker pull ghcr.io/bizinmitya/front-react-avito:latest.
5. Запустить Docker image с помощью команды docker run -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:latest.
6. Запустить метод main.
7. Перейти по адресу: http://localhost:3000/

Разработчик:
https://github.com/DmitryM321