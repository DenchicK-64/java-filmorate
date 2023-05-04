package ru.yandex.practicum.filmorate.storage.film;

public class FilmSqlRequestList {
    public static final String CREATE_FILM = "INSERT INTO films (title, description, release_date, duration, mpa_id, likes_counter) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_FILM = "UPDATE films SET title = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ?, likes_counter = ? WHERE film_id = ?";
    public static final String FIND_ALL_FILMS = "SELECT * FROM films INNER JOIN mpa on films.mpa_id = mpa.mpa_id";
    public static final String GET_FILM = "SELECT * FROM films JOIN mpa on films.mpa_id = mpa.mpa_id WHERE films.film_id = ?";
    public static final String ADD_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
    public static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    public static final String GET_POPULAR_FILMS = "SELECT films.film_id, films.title, films.description, " +
            "films.release_date, films.duration, films.likes_counter, mpa.mpa_id, mpa.name, FROM films JOIN mpa ON films.mpa_id = mpa.mpa_id " +
            "GROUP BY films.film_id " +
            "ORDER BY films.likes_counter DESC LIMIT ?";

    public static final String GET_FILM_LIKES = "SELECT user_id FROM likes WHERE film_id = ?";

    public static final String FILM_CHECK_IN_DB = "SELECT exists (SELECT * FROM films WHERE film_id = ?)";

    /*public static final String GET_POPULAR_FILMS = "SELECT films.film_id, films.title, films.description, films.release_date, " +
            "films.duration, films.likes_counter, mpa.name, genres.name " +
            "FROM films INNER JOIN mpa ON films.mpa_id = mpa.mpa_id " +
            "LEFT JOIN film_genres ON films.film_id = film_genres.film_id " +
            "LEFT JOIN genres ON film_genres.genre_id = genres.genre_id WHERE film_genres.genre_id IN (SELECT genres.genre_id FROM genres) " +
            "GROUP BY films.film_id " +
            "ORDER BY films.likes_counter DESC LIMIT ?";*/

    /*public static final String GET_POPULAR_FILMS = "SELECT films.film_id, films.title, films.description, films.release_date, films.duration, genres.genre_id, genres.name, mpa.mpa_id, mpa.name, films.likes_counter FROM films INNER JOIN mpa on films.mpa_id = mpa.mpa_id WHERE films.film_id IN (SELECT film_id FROM film_genres WHERE genre_id IN (SELECT genre_id FROM genres))";*/
}