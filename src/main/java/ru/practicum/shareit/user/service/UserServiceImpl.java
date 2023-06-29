package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().values().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, id);
        return UserDtoMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        return UserDtoMapper.toUserDto(userRepository.saveUser(user));
    }

    @Override
    public UserDto updateUser(Long patchId, UserDto userDto) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, patchId);
        User user = UserDtoMapper.toUser(userDto);
        return UserDtoMapper.toUserDto(userRepository.updateUser(patchId, user));
    }

    @Override
    public void deleteUser(Long removeId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, removeId);
        userRepository.deleteUser(removeId);
    }
}
