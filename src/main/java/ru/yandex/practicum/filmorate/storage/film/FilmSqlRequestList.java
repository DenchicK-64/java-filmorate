package ru.yandex.practicum.filmorate.storage.film;

public class FilmSqlRequestList {
    public static final String CREATE_FILM = "INSERT INTO films (title, description, release_date, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_FILM = "UPDATE films SET title = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_id = ? WHERE film_id = ?";
    public static final String FIND_ALL_FILMS = "SELECT * FROM films INNER JOIN mpa on films.mpa_id = mpa.mpa_id";
    public static final String GET_FILM = "SELECT * FROM films JOIN mpa on films.mpa_id = mpa.mpa_id WHERE films.film_id = ?";
    public static final String ADD_LIKE = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    public static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    public static final String GET_POPULAR_FILMS = "SELECT films.*, mpa.*, FROM films " +
            "LEFT JOIN mpa ON films.mpa_id = mpa.mpa_id " +
            "GROUP BY films.film_id " +
            "ORDER BY films.likes_counter DESC LIMIT ?";

    public static final String GET_FILM_LIKES = "SELECT user_id FROM likes WHERE film_id = ?";

    public static final String FILM_CHECK_IN_DB = "SELECT exists (SELECT * FROM films WHERE film_id = ?)";
}