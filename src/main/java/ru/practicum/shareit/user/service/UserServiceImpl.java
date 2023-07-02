package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;
import ru.practicum.shareit.validator.UserFieldsValidator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDBRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
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
        UserFieldsValidator.checkUserDoesntExist(userRepository, patchId);
        User updateUser = UserDtoMapper.toUser(userDto);
        if (updateUser.getName() != null) {
            userRepository.updateName(patchId, updateUser.getName());
        }
        if (updateUser.getEmail() != null) {
            try {
                userRepository.updateEmail(patchId, updateUser.getEmail());
            } catch (DataIntegrityViolationException e) {
                throw new EmailException("Email already exist");
            }
        }
        return UserDtoMapper.toUserDto(userRepository.findById(patchId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist")));
    }

    @Override
    @Transactional
    public void deleteUser(Long removeId) {
        userRepository.deleteById(removeId);
    }
}
