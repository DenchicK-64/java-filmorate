package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    User testUser;
    User testUserTwo;

    @BeforeEach
    public void setUp() {
        testUser = new User("address@somemail.ru", "Some_login", "Some_name",
                LocalDate.of(2000, 1, 1), new HashSet<>());
        testUserTwo = new User("anotheraddress@somemail.ru", "Some_login2", "Some_name",
                LocalDate.of(1990, 1, 1), new HashSet<>());
    }

    @Test
    public void createUser() {
        userDbStorage.create(testUser);
        final User testUserCopy = userDbStorage.getUser(testUser.getId());
        assertNotNull(testUserCopy);
        assertEquals(testUserCopy, testUser);
        Collection<User> testCollection = userDbStorage.findAll();
        assertEquals(1, testCollection.size());
        assertTrue(testCollection.contains(testUser));
    }

    @Test
    public void updateTest() {
        userDbStorage.create(testUser);
        testUser.setName("Update");
        testUser.setLogin("Test_Login");
        testUser.setEmail("test@test.com");
        userDbStorage.update(testUser);
        final User testUserCopy = userDbStorage.getUser(testUser.getId());
        assertNotNull(testUserCopy);
        assertEquals(testUserCopy, testUser);
    }

    @Test
    public void findAllTest() {
        userDbStorage.create(testUser);
        userDbStorage.create(testUserTwo);
        final Collection<User> testCollection = userDbStorage.findAll();
        assertNotNull(testCollection);
        assertEquals(2, testCollection.size());
        assertEquals(testCollection, userDbStorage.findAll());
    }

    @Test
    public void getUserTest() {
        userDbStorage.create(testUser);
        final User testUserCopy = userDbStorage.getUser(testUser.getId());
        assertNotNull(testUserCopy);
        assertEquals(testUserCopy, testUser);
    }

    @Test
    public void addFriendTest() {
        userDbStorage.create(testUser);
        userDbStorage.create(testUserTwo);
        userDbStorage.addFriend(testUser.getId(), testUserTwo.getId());
        assertEquals(userDbStorage.getUserFriends(testUser.getId()).size(), 1);
        assertEquals(List.of(testUserTwo), userDbStorage.getUserFriends(testUser.getId()));
    }

    @Test
    public void deleteFriendTest() {
        userDbStorage.create(testUser);
        userDbStorage.create(testUserTwo);
        userDbStorage.addFriend(testUser.getId(), testUserTwo.getId());
        userDbStorage.deleteFriend(testUser.getId(), testUserTwo.getId());
        assertTrue(userDbStorage.getUserFriends(testUser.getId()).isEmpty());
    }

    @Test
    public void getCommonFriendsTest() {
        userDbStorage.create(testUser);
        userDbStorage.create(testUserTwo);
        User testUserThree = new User("test@testmail.ru", "three333", "Third",
                LocalDate.of(1995, 1, 1), new HashSet<>());
        userDbStorage.create(testUserThree);
        userDbStorage.addFriend(testUser.getId(), testUserThree.getId());
        userDbStorage.addFriend(testUserTwo.getId(), testUserThree.getId());
        assertEquals(List.of(testUserThree), userDbStorage.getCommonFriends(testUser.getId(), testUserTwo.getId()));
    }

    @Test
    void shouldCreateUserWithWrongLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.create(new User(1, "address@somemail.ru", " ", "Some_name",
                                LocalDate.of(2000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithWrongEmail() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.create(new User(1, "addresssomemail.ru", "Some_login",
                                "Some_name", LocalDate.of(2000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithWrongBirthday() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.create(new User(1, "address@somemail.ru", "Some_login",
                                "Some_name", LocalDate.of(3000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithWrongName() {
        User noName = new User(1, "address@somemail.ru", "Some_login", " ",
                LocalDate.of(2000, 1, 1), new HashSet<>());
        userDbStorage.create(noName);
        assertEquals(noName.getLogin(), noName.getName());
    }

    @Test
    void shouldUpdateUserWithWrongId() {
        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.update(new User(1000, "address@somemail.ru", "Some_login",
                                "Some_name", LocalDate.of(2000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Пользователь не найден в базе данных", exception.getMessage());
    }

    @Test
    void shouldGetUserWithWrongId() {
        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.getUser(100);
                    }
                });
        assertEquals("Пользователь не найден в базе данных", exception.getMessage());
    }

    @Test
    void shouldAddFriendWithWrongId() {
        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userDbStorage.addFriend(testUser.getId(), -1);
                    }
                });
        assertEquals("Пользователь не найден в базе данных", exception.getMessage());
    }
}