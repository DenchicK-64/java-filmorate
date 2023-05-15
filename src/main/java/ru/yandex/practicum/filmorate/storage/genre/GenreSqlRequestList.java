package ru.yandex.practicum.filmorate.storage.genre;

public class GenreSqlRequestList {
    public static final String FIND_ALL_GENRES = "SELECT * FROM genres ORDER BY genre_id";

    public static final String GET_GENRE = "SELECT * FROM genres WHERE genre_id = ?";

    public static final String GET_FILM_GENRES = "SELECT * FROM genres WHERE genre_id IN (SELECT genre_id FROM film_genres WHERE film_id = ? ORDER BY film_id)";

    public static final String SET_FILM_GENRE = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    public static final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id = ?";

    public static final String CHECK_GENRE_IN_DB = "SELECT exists (SELECT * FROM genres WHERE genre_id = ?)";
}