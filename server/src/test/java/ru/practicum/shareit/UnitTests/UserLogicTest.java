package ru.practicum.shareit.UnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDBRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@SpringBootTest
public class UserLogicTest {
    @Mock
    UserDBRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    private final User user = new User(
            1L,
            "John",
            "first@user.com"
    );
    private final UserDto userDto = new UserDto(
            1L,
            "John",
            "first@user.com"
    );

    @Test
    void getAllUserTest() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(user));
        List<UserDto> users = userService.getAllUsers();
        assertThat(users).hasSize(1).contains(userDto);
    }

    @Test
    void getUserByIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto getUserDto = userService.getUserById(1L);
        assertEquals(userDto, getUserDto);
    }

    @Test
    void getUserByIdNotFoundExceptionTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(999L));
        assertEquals("User with id %d does not exist", ex.getMessage());
    }

    @Test
    void saveUserTest() {
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserDto getUserDto = userService.saveUser(userDto);
        assertEquals(userDto, getUserDto);
    }

    @Test
    void saveUserFailEmailTest() {
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exist"));
        EmailException ex = Assertions.assertThrows(EmailException.class, () -> userService.saveUser(userDto));
        assertEquals("Email already exist", ex.getMessage());
    }

    @Test
    void updateUserTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenReturn(user);
        UserDto getUserDto = userService.updateUser(1L, userDto);
        assertEquals(userDto, getUserDto);
    }

    @Test
    void updateUserFailEmailTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exist"));
        EmailException ex = Assertions.assertThrows(EmailException.class, () -> userService.updateUser(1L, userDto));
        assertEquals("Email already exist", ex.getMessage());
    }

    @Test
    void deleteUserTest() {
        doNothing().when(userRepository)
                .deleteById(anyLong());
        userService.deleteUser(1L);
        Mockito.verify(userRepository, times(1))
                .deleteById(anyLong());
    }
}
