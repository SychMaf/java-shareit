package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long patchId, UserDto userDto);

    void deleteUser(Long removeId);
}
