package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreStorage {
    List<Genre> findAllGenres();

    Genre getGenre(int genreId);

    List<Genre> getFilmGenres(int filmId);

    void setFilmGenres(int filmId, List<Genre> genres);

    void deleteFilmGenres(int filmId);

    Map<Integer, Film> getGenresFromFilms(Map<Integer, Film> films);
}