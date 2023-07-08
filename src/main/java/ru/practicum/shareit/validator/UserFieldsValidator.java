package ru.practicum.shareit.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;

@UtilityClass
public class UserFieldsValidator {

    public void checkUserDoesntExist(UserDBRepository userRepository, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(
                    String.format("User with id %d does not exist", userId)
            );
        }
    }
}
