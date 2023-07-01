package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface UserRepository {
    Map<Long, User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User user);

    User updateUser(Long patchId, User user);

    void deleteUser(Long removeId);
}
