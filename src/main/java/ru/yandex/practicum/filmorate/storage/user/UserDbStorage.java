package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("UserDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


   /* @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }*/
    @Override
    public User create(User user) {
        ValidateUser.validateUser(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(UserSqlRequestList.CREATE_USER,
                    new String[]{"user_id"});
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        userCheckInDb(user.getId());
        ValidateUser.validateUser(user);
        jdbcTemplate.update(UserSqlRequestList.UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(UserSqlRequestList.FIND_ALL_USERS, this::makeUser);
    }

    @Override
    public User getUser(int userId) {
        userCheckInDb(userId);
        return jdbcTemplate.queryForObject(UserSqlRequestList.GET_USER, this::makeUser, userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        userCheckInDb(friendId);
        SqlRowSet checkFriendShipStatus = jdbcTemplate.queryForRowSet(
                UserSqlRequestList.CHECK_FRIENDSHIP_STATUS, friendId, userId);
        if (checkFriendShipStatus.next()) {
            jdbcTemplate.update(UserSqlRequestList.UPDATE_FRIEND_STATUS, true, friendId, userId, false);
            jdbcTemplate.update(UserSqlRequestList.ADD_FRIEND, userId, friendId, true);
        } else {
            jdbcTemplate.update(UserSqlRequestList.ADD_FRIEND, userId, friendId, false);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        SqlRowSet checkFriendShipStatus = jdbcTemplate.queryForRowSet(
                UserSqlRequestList.CHECK_FRIENDSHIP_STATUS, friendId, userId);
        if (checkFriendShipStatus.next()) {
            jdbcTemplate.update(UserSqlRequestList.UPDATE_FRIEND_STATUS, false, friendId, userId, userId);
            jdbcTemplate.update(UserSqlRequestList.DELETE_FRIEND, userId, friendId);
        } else {
            jdbcTemplate.update(UserSqlRequestList.DELETE_FRIEND, userId, friendId);
        }
    }

    @Override
    public List<User> getUserFriends(int userId) {
        return jdbcTemplate.query(UserSqlRequestList.GET_USER_FRIENDS, this::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        return jdbcTemplate.query(UserSqlRequestList.GET_COMMON_FRIENDS, this::makeUser, userId, friendId);
    }

    @Override
    public void userCheckInDb(int userId) {
        Boolean checkUser = jdbcTemplate.queryForObject(UserSqlRequestList.USER_CHECK_IN_DB, Boolean.class, userId);
        if (Boolean.FALSE.equals(checkUser)) {
            throw new UserNotFoundException("Пользователь не найден в базе данных");
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"), rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}