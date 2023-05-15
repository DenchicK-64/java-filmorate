package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getMpaTest() {
        Mpa testMpa = mpaDbStorage.getMpa(1);
        assertThat(testMpa).hasFieldOrPropertyWithValue("id", 1);
        assertThat(testMpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void findAllMpaTest() {
        assertEquals(mpaDbStorage.findAllMpa().size(), 5);
    }

    @Test
    void shouldGetMpaWithWrongId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        mpaDbStorage.getMpa(100);
                    }
                });
        assertEquals("Рейтинг не найден в базе данных", exception.getMessage());
    }
}