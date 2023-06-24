package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto saveUser(User user);

    UserDto updateUser(Long patchId, Map<String, Object> updates);

    void deleteUser(Long removeId);
}
