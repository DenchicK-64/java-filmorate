package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service

public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, @Autowired(required = false) UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        log.info("Получение всех фильмов");
        return filmStorage.findAll();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userService.getUser(userId);
        film.getLikes().add(user.getId());
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userService.getUser(userId);
        film.getLikes().remove(user.getId());
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> popularFilms = findAll()
                .stream()
                .filter(film -> film.getLikes() != null)
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        return popularFilms;
    }
}