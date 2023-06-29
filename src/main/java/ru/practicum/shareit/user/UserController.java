package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validator.ValidationGroups;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUser() {
        log.trace("Got request to get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.trace("Got request to find user with id: {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    @Validated(ValidationGroups.Create.class)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        log.trace("Got request to create user: {}", userDto);
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    @Validated(ValidationGroups.Update.class)
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody @Valid UserDto userDto) {
        log.trace("Got request to update user: {}", userDto);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.trace("Got request to delete user with id: {}", id);
        userService.deleteUser(id);
    }
}
