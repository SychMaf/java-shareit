package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUser() {
        log.trace("SERVER: request to get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.trace("SERVER: request to find user with id: {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        log.trace("SERVER: request to create user: {}", userDto);
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody UserDto userDto) {
        log.trace("SERVER: request from user: {} to update user: {}", id, userDto);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.trace("SERVER: request to delete user with id: {}", id);
        userService.deleteUser(id);
    }
}
