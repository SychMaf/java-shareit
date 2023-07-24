package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ValidationGroups;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        log.trace("GATEWAY: request to get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.trace("GATEWAY: request to find user with id: {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping
    @Validated(ValidationGroups.Create.class)
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto) {
        log.trace("GATEWAY: request to create user: {}", userDto);
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{id}")
    @Validated(ValidationGroups.Update.class)
    public ResponseEntity<Object> updateUser(@PathVariable long id,
                              @RequestBody @Valid UserDto userDto) {
        log.trace("GATEWAY: request from user: {} to update user: {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.trace("GATEWAY: request to delete user with id: {}", id);
        userClient.deleteUser(id);
    }
}
