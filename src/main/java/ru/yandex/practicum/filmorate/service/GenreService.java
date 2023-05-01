package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> findAllGenres() {
        return genreStorage.findAllGenres();
    }

    public Genre getGenre(int filmId) {
        return genreStorage.getGenre(filmId);
    }

    public void setFilmGenres(int filmId, List<Genre> genres) {
        genreStorage.setFilmGenres(filmId, genres);
    }

    public void deleteFilmGenres(int filmId) {
        genreStorage.deleteFilmGenres(filmId);
    }
}