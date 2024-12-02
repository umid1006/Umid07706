package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User createUser(User user) {
        validateUser(user);
        int newId = generateId();
        user.setId(newId);
        users.add(user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("ID пользователя должен быть указан");
        }
        User existingUser = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException("Пользователь с ID " + user.getId() + " не найден"));

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getLogin() != null) {
            existingUser.setLogin(user.getLogin());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            existingUser.setName(user.getName());
        } else {
            existingUser.setName(existingUser.getLogin());
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }
        return existingUser;
    }

    private int generateId() {
        int maxId = users.stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}