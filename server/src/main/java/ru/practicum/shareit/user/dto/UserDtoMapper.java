package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserDtoMapper {

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }

    public User updateUser(UserDto userDto, User user) {
        return new User(
                user.getId(),
                userDto.getName() != null ? userDto.getName() : user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail()
        );
    }
}
