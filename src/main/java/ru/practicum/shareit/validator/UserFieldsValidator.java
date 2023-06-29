package ru.practicum.shareit.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

@UtilityClass
public class UserFieldsValidator {

    public void checkUserDoesntExist(UserRepository userRepository, Long userId) {
        if (!userRepository.getAllUsers().containsKey(userId)) {
            throw new NotFoundException(
                    String.format("User with id %d does not exist", userId)
            );
        }
    }
}
