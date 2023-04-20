package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

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

    @Override
    public Film create(Film film) {
        if (validateFilm(film)) {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.info("Фильм добавлен: {}", film.getName());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Нельзя выполнить обновление: фильм не найден в базе данных");
            throw new FilmNotFoundException("Нельзя выполнить обновление: фильм не найден в базе данных");
        }
        if (validateFilm(film)) {
            films.put(film.getId(), film);
            log.info("Фильм добавлен: {}", film.getName());
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        log.info("Получение всех фильмов");
        return films.values();
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден в базе данных");
        }
        return films.get(id);
    }
}