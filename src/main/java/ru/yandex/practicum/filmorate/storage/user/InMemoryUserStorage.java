package ru.yandex.practicum.filmorate.storage.user;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryUserStorage")
@NoArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    ValidateUser validateUser = new ValidateUser();
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User create(User user) {
        validateUser.validate(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user.getName());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Нельзя выполнить обновление: пользователь не найден в базе данных");
            throw new UserNotFoundException("Нельзя выполнить обновление: пользователь не найден в базе данных");
        }
        validateUser.validate(user);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user.getName());
        return user;
    }

    @Override
    public Collection<User> findAll() {
        log.info("Получение всех фильмов");
        return users.values();
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь не найден в базе данных");
        }
        return users.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        User user = getUser(userId);
        return findAll().stream()
                .filter(p -> user.getFriends().contains(p.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        Set<Integer> commonFriendsId = new HashSet<>(getUser(userId).getFriends());
        commonFriendsId.retainAll(getUser(friendId).getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonFriendsId) {
            commonFriends.add(getUser(id));
        }
        return commonFriends;
    }
}