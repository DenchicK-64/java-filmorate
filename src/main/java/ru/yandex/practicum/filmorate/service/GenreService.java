package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
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

    public List<Genre> getFilmGenres(int filmId) {return genreStorage.getFilmGenres(filmId);}

    public Map<Integer, Film> getGenresFromFilms(Map<Integer, Film> films){
        return genreStorage.getGenresFromFilms(films);
    }
}