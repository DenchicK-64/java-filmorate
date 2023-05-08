/*package ru.yandex.practicum.filmorate.InMemoryStorageTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    UserController userController;
    User testUser;
    User testUserTwo;

    @BeforeEach
    void setUp() {
        testUser = new User("address@somemail.ru", "Some_login", "Some_name",
                LocalDate.of(2000, 1, 1), new HashSet<>());
        testUserTwo = new User("anotheraddress@somemail.ru", "Some_login2", "Some_name",
                LocalDate.of(1990, 1, 1), new HashSet<>());
    }

    @AfterEach
    void clear() {
        userController.findAll().clear();
    }

    @Test
    void createUser() {
        userController.create(testUser);
        final User testUserCopy = testUser;
        assertNotNull(testUser);
        assertEquals(testUserCopy, testUser);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void getAllUsers() {
        userController.create(testUser);
        userController.create(testUserTwo);
        final Collection<User> testCollection = userController.findAll();
        assertNotNull(testCollection);
        assertEquals(2, testCollection.size());
        assertEquals(testCollection, userController.findAll());
    }

    @Test
    void shouldCreateUserWithWrongLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.create(new User(1, "address@somemail.ru", " ",
                                "Some_name", LocalDate.of(2000, 1, 1), new HashSet<>()));
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
                        userController.create(new User(1, "addresssomemail.ru", "Some_login",
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
                        userController.create(new User(1, "address@somemail.ru", "Some_login",
                                "Some_name", LocalDate.of(3000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Дата рождения не может быть в будущем!", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithWrongName() {
        User noName = new User(1, "address@somemail.ru", "Some_login", " ",
                LocalDate.of(2000, 1, 1), new HashSet<>());
        userController.create(noName);
        assertEquals(noName.getLogin(), noName.getName());
    }

    @Test
    void shouldUpdateUserWithWrongId() {
        final UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userController.update(new User(10000, "address@somemail.ru", "Some_login",
                                "Some_name", LocalDate.of(2000, 1, 1), new HashSet<>()));
                    }
                });
        assertEquals("Пользователь не найден в базе данных", exception.getMessage());
    }
}*/