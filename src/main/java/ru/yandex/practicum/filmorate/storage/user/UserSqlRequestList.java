package ru.yandex.practicum.filmorate.storage.user;

public class UserSqlRequestList {
    public static final String CREATE_USER = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";

    public static final String FIND_ALL_USERS = "SELECT * FROM users";

    public static final String GET_USER = "SELECT * FROM users WHERE user_id = ?";

    public static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";

    public static final String DELETE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

    public static final String CHECK_FRIENDSHIP_STATUS = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";

    static final String UPDATE_FRIEND_STATUS = "UPDATE friends SET status = ? WHERE user_id = ? " +
            "AND friend_id = ? AND status = ?";

    public static final String GET_USER_FRIENDS = "SELECT * FROM users LEFT JOIN friends ON " +
            "users.user_id = friends.friend_id WHERE friends.user_id = ?";

    public static final String GET_COMMON_FRIENDS = "SELECT * FROM users LEFT JOIN friends ON " +
            "users.user_id = friends.friend_id WHERE friends.user_id = ? AND friends.friend_id IN " +
            "(SELECT friend_id FROM friends LEFT JOIN users ON users.user_id  = friends.friend_id " +
            "WHERE friends.user_id = ?)";

    public static final String USER_CHECK_IN_DB = "SELECT exists (SELECT * FROM users WHERE user_id = ?)";
}