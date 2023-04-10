package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    private boolean validateUser(@Valid User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        return true;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (validateUser(user)) {
            user.setId(userId++);
            users.put(user.getId(), user);
            log.info("Пользователь добавлен: {}", user.getName());
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Нельзя выполнить обновление: пользователь не найден в базе данных");
            throw new ValidationException("Нельзя выполнить обновление: пользователь не найден в базе данных");
        }
        if (validateUser(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь добавлен: {}", user.getName());
        }
        return user;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получение всех фильмов");
        return users.values();
    }
}