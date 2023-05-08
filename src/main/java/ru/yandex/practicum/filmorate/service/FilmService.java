package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.ValidateFilm;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
/*@AllArgsConstructor*/
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, @Autowired(required = false) @Qualifier("UserDbStorage") UserStorage userStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        Film filmUpd = filmStorage.update(film);
        Map<Integer, Film> filmMap = new HashMap<>();
        filmMap.put(filmUpd.getId(), film);
        return genreStorage.getGenresFromFilms(filmMap).get(filmUpd.getId());
    }

    public List<Film> findAll() {
        log.info("Получение всех фильмов");
        List<Film> films = filmStorage.findAll();
        Map<Integer, Film> filmMap = new HashMap<>();
        for(Film film : films) {
        filmMap.put(film.getId(), film);
    }
        return new ArrayList<>(genreStorage.getGenresFromFilms(filmMap).values());
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        Map<Integer, Film> filmMap = new HashMap<>();
        filmMap.put(film.getId(), film);
        return genreStorage.getGenresFromFilms(filmMap).get(film.getId());
    }

    public void addLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    public void deleteLike(Integer filmId, Integer userId) {
        userStorage.userCheckInDb(userId);
        Film film = getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        Map<Integer, Film> filmMap = new HashMap<>();
        for(Film film : films) {
            filmMap.put(film.getId(), film);
        }
        return new ArrayList<>(genreStorage.getGenresFromFilms(filmMap).values());
    }
}