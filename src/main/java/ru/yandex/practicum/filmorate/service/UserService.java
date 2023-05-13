package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) throws ValidationException {
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public List<User> getUserFriends(int userId) {
        return userStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public void userCheckInDb(int userId) {
        userStorage.userCheckInDb(userId);
    }
}