package ru.practicum.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class NewUserDto {

    @NotBlank(message = "Почта пользователя обязательна для заполнения")
    @Email(message = "Передан неправильный формат email")
    private String email;

    @NotBlank(message = "Имя пользователя обязательно для заполнения")
    private String name;
}
