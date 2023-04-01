package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 г.");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.info("Фильм добавлен: {}", film.getName());
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Нельзя выполнить обновление: фильм не найден в базе данных");
            throw new ValidationException("Нельзя выполнить обновление: фильм не найден в базе данных");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 г.");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        } else {
            films.put(film.getId(), film);
            log.info("Фильм добавлен: {}", film.getName());
        }

        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получение всех фильмов");
        return films.values();
    }
}