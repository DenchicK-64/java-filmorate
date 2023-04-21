# java-filmorate
Template repository for Filmorate project.

**Диаграмма база данных**

<img alt="”drawSQL-filmorate-export-2023-04-20”" src="src\main\resources\drawSQL-filmorate-export-2023-04-20.png" width ="””" height="””">

**Примеры запросов**

Получение списка пользователей:

SELECT *
FROM users

Получение информации о пользователе по id:

SELECT *
FROM users
WHERE user_id = ?