package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequestMapping("/mpa")
@RestController
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;
    /*@Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }*/


    @GetMapping
    public List<Mpa> findAllMpa() {
        return mpaService.findAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        return mpaService.getMpa(id);
    }
}