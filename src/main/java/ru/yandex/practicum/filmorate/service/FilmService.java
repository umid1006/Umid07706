package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(nextId++);
        films.put(film.getId(), film);

        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с ID " + film.getId() + " не найден");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    public static void validateFilm(Film film) {
        if
        (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (film.getDescription()
                != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");

        }

        LocalDate releaseDate = film.getReleaseDate();
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    public Film getFilmById(int id) {
        Film film = films.get(id); // Assuming 'films' is your Map or database access mechanism
        if (film == null) {
            throw new FilmNotFoundException("Film with ID " + id + " not found.");
        }
        return film;
    }
}