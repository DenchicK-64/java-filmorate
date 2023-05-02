package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        for (Genre genre : genres) {
            jdbcTemplate.update(GenreSqlRequestList.SET_FILM_GENRE, filmId, genre.getId());
        }
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        jdbcTemplate.update(GenreSqlRequestList.DELETE_FILM_GENRES, filmId);
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