package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE) // Add this line
public class User {
    int id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть корректного формата")
    String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    String login;

    String name;

    @NotNull(message = "Дата рождения не может быть null")
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}