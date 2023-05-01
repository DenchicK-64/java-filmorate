package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAllGenres();

    Genre getGenre(int genreId);

    List<Genre> getFilmGenres(int filmId);

    void setFilmGenres(int filmId, List<Genre> genres);

    void deleteFilmGenres(int filmId);
}