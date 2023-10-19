package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

//    @NotBlank(message = "Почта пользователя обязательна для заполнения")
//    @Email(message = "Передан неправильный формат email")
    private String email;

    private Long id;

//    @NotBlank(message = "Имя пользователя обязательно для заполнения")
    private String name;
}
