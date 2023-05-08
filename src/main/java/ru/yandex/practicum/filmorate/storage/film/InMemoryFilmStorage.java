package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.ValidateFilm;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film create(Film film) {
        ValidateFilm.validateFilm(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        ValidateFilm.validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film.getName());
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.info("Получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("Фильм не найден в базе данных");
        }
        return films.get(filmId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return findAll()
                .stream()
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}