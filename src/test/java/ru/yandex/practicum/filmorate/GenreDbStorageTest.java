package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    public void getGenreTest() {
        Genre testGenre = genreDbStorage.getGenre(1);
        assertThat(testGenre).hasFieldOrPropertyWithValue("id", 1);
        assertThat(testGenre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    public void findAllGenresTest() {
        assertEquals(genreDbStorage.findAllGenres().size(), 6);
    }

    @Test
    void shouldGetGenreWithWrongId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        genreDbStorage.getGenre(100);
                    }
                });
        assertEquals("Жанр не найден в базе данных", exception.getMessage());
    }
}