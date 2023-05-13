package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        return jdbcTemplate.query(GenreSqlRequestList.FIND_ALL_GENRES, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenre(int genreId) {
        genreCheckInDb(genreId);
        return jdbcTemplate.queryForObject(GenreSqlRequestList.GET_GENRE, (rs, rowNum) -> makeGenre(rs), genreId);
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        return jdbcTemplate.query(GenreSqlRequestList.GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    @Override
    public void setFilmGenres(int filmId, List<Genre> genres) {
        try {
            List<Genre> unique = genres.stream().distinct().collect(Collectors.toList());
            genres.clear();
            genres.addAll(unique);
            jdbcTemplate.batchUpdate(GenreSqlRequestList.SET_FILM_GENRE, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        } catch (DataIntegrityViolationException exception) {
            System.out.println("Дубликаты не допустимы");
        }
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        jdbcTemplate.update(GenreSqlRequestList.DELETE_FILM_GENRES, filmId);
    }

    @Override
    public Map<Integer, Film> getGenresFromFilms(Map<Integer, Film> films) {
        Set<Integer> filmIds = films.keySet();
        if (filmIds.isEmpty()) {
            return films;
        }
        SqlParameterSource parameters = new MapSqlParameterSource("filmsId", filmIds);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = "SELECT genres.genre_id, genres.name, film_genres.film_id FROM genres INNER JOIN " +
                "film_genres ON genres.genre_id = film_genres.genre_id WHERE film_genres.film_id IN (:filmsId)";
        namedParameterJdbcTemplate.query(sql, parameters, (rs, rowNum) -> {
            Film film = films.get(rs.getInt("film_id"));
            return film.getGenres().add(
                    new Genre(
                            rs.getInt("genre_id"),
                            rs.getString("name")
                    ));
        });
        return films;
    }

    private void genreCheckInDb(int genreId) {
        Boolean checkGenre = jdbcTemplate.queryForObject(GenreSqlRequestList.CHECK_GENRE_IN_DB,
                Boolean.class, genreId);
        if (Boolean.FALSE.equals(checkGenre)) {
            throw new NotFoundException("Жанр не найден в базе данных");
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"),
                rs.getString("name"));
    }
}