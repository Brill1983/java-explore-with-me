package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ElementNotFoundException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(Set<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);

        Page<User> userPage = CollectionUtils.isEmpty(ids) ?
                userRepository.findAll(page) :
                userRepository.findAllByIdIn(ids, page);

        return userPage.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(NewUserDto newUserDto) {
        User user = userRepository.save(UserMapper.toUser(newUserDto));
        userRepository.findByName(newUserDto.getEmail())
                .ifPresent(cat -> {
                    throw new ConflictException("Пользователь с email " + newUserDto.getEmail() + "уже существует");
                });
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("Пользователь с ID: " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }
}
