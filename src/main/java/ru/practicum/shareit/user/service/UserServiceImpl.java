package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserFieldsValidator userFieldsValidator;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().values().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        userFieldsValidator.checkUserDoesntExist(id);
        return UserDtoMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public UserDto saveUser(User user) {
        return UserDtoMapper.toUserDto(userRepository.saveUser(user));
    }

    @Override
    public UserDto updateUser(Long patchId, Map<String, Object> updates) {
        userFieldsValidator.checkUserDoesntExist(patchId);
        return UserDtoMapper.toUserDto(userRepository.updateUser(patchId, updates));
    }

    @Override
    public void deleteUser(Long removeId) {
        userFieldsValidator.checkUserDoesntExist(removeId);
        userRepository.deleteUser(removeId);
    }
}
