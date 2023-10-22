package ru.practicum.user;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public User toUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
