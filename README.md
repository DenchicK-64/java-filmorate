# java-filmorate
Template repository for Filmorate project.

**Диаграмма базы данных**

![](C:\Users\sid-t\DELETE\java-filmorate\src\main\resources\drawSQL-filmorate-export-2023-04-20.png)

**Примеры запросов**

Получение списка пользователей:

SELECT *
FROM users

Получение информации о пользователе по id:

SELECT *
FROM users
WHERE user_id = ?