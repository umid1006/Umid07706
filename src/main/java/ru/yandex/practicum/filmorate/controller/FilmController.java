package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAllFilms();
    }

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        try {
            Film updatedFilm = filmService.updateFilm(film);
            return ResponseEntity.ok(updatedFilm);
        } catch (FilmNotFoundException e) { // Catch the specific exception from your FilmService
            // Handle ResourceNotFoundException specifically
            log.warn("Film not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or a custom error response object
        } catch (Exception e) {
            // Catch any other exceptions
            log.error("Error updating film", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}") // Add this endpoint
    public ResponseEntity<Film> getFilmById(@PathVariable int id) {
        try {
            // Attempt to get the film by ID
            Film film = filmService.getFilmById(id);
            return ResponseEntity.ok(film); // Return 200 OK if found
        } catch (FilmNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if not found
        }
    }
}