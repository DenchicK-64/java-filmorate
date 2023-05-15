# java-filmorate
Template repository for Filmorate project.

**Диаграмма базы данных**

![](https://github.com/DenchicK-64/java-filmorate/blob/add-database/drawSQL-filmorate-export-2023-04-21.png)

**Примеры запросов**

Получение списка пользователей:

SELECT *
FROM users

Получение информации о пользователе по id:

SELECT *
FROM users
WHERE user_id = ?
