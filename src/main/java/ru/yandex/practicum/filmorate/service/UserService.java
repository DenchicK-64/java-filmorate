package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) throws ValidationException {
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        user.getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUser(userId);
        user.getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public List<User> getUserFriends(int userId) {
        User user = getUser(userId);
        return userStorage.findAll().stream()
                .filter(p -> user.getFriends().contains(p.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        Set<Integer> commonFriendsId = new HashSet<>(userStorage.getUser(userId).getFriends());
        commonFriendsId.retainAll(userStorage.getUser(friendId).getFriends());

        return userStorage.findAll().stream()
                .filter(p -> commonFriendsId.contains(p.getId()))
                .collect(Collectors.toList());
    }
}