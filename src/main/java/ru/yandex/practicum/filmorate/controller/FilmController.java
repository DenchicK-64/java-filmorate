package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Фильм добавлен: {}", film.getName());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Фильм добавлен: {}", film.getName());
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Получение всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) throws Throwable {
        return filmService.getFilm(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer filmId, @PathVariable Integer userId) throws Throwable {
        log.info("Пользователь ставит лайк фильму");
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) throws Throwable {
        log.info("Пользователь удаляет лайк у фильма");
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Запрос списка популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}