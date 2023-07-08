package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDBRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        return UserDtoMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        try {
            User saveUser = userRepository.save(UserDtoMapper.toUser(userDto));
            return UserDtoMapper.toUserDto(saveUser);
        } catch (DataIntegrityViolationException e) {
            throw new EmailException("Email already exist");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long patchId, UserDto userDto) {
        User outdatedUser = userRepository.findById(patchId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        User updateUser = UserDtoMapper.updateUser(userDto, outdatedUser);
        try {
            User patchUser = userRepository.save(updateUser);
            return UserDtoMapper.toUserDto(patchUser);
        } catch (DataIntegrityViolationException e) {
            throw new EmailException("Email already exist");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long removeId) {
        userRepository.deleteById(removeId);
    }
}
