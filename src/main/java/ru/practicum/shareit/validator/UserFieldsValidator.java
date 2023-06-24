package ru.practicum.shareit.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserFieldsValidator {
    private final UserRepository userRepository;

    public void checkUserDoesntExist(Long userId) {
        if (!userRepository.getAllUsers().containsKey(userId)) {
            throw new NotFoundException(
                    String.format("User with id %d does not exist", userId)
            );
        }
    }
}
