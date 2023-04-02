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
    static final int MAX_DESCRIPTION_LENGTH = 200;
    static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private boolean validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.error("Дата релиза — не раньше 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 г.");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }
        return true;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (validateFilm(film)) {
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
        if (validateFilm(film)) {
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