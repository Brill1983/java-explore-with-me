package ru.practicum.user;

import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getUsers(Set<Long> ids, Integer from, Integer size);

    UserDto saveUser(NewUserDto newUserDto);

    void deleteUser(long userId);
}
