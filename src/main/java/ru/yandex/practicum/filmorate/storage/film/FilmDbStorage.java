package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDbStorage")
@Slf4j
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film create(Film film) {
        ValidateFilm.validateFilm(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(FilmSqlRequestList.CREATE_FILM, new String[]{"film_id"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            genreStorage.deleteFilmGenres(film.getId());
            genreStorage.setFilmGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        filmCheckInDb(film.getId());
        ValidateFilm.validateFilm(film);
        jdbcTemplate.update(FilmSqlRequestList.UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        genreStorage.deleteFilmGenres(film.getId());
        if (film.getGenres() != null) {
            genreStorage.setFilmGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(FilmSqlRequestList.FIND_ALL_FILMS, this::makeFilm);
    }

    @Override
    public Film getFilm(int filmId) {
        filmCheckInDb(filmId);
        return jdbcTemplate.queryForObject(FilmSqlRequestList.GET_FILM, this::makeFilm, filmId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update(FilmSqlRequestList.ADD_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update(FilmSqlRequestList.DELETE_LIKE, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(FilmSqlRequestList.GET_POPULAR_FILMS, this::makeFilm, count);
    }

    public void filmCheckInDb(int filmId) {
        Boolean checkFilm = jdbcTemplate.queryForObject(FilmSqlRequestList.FILM_CHECK_IN_DB, Boolean.class, filmId);
        if (Boolean.FALSE.equals(checkFilm)) {
            throw new FilmNotFoundException("Фильм не найден в базе данных");
        }
    }

    private List<Integer> getFilmLikes(int filmId) {
        return jdbcTemplate.queryForList(FilmSqlRequestList.GET_FILM_LIKES, Integer.class, filmId);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("name")),
                new ArrayList<>());
    }
}