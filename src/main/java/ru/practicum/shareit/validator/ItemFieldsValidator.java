package ru.practicum.shareit.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemFieldsValidator {
    private final ItemRepository itemRepository;

    public void checkItemToUser(Long userId, Long itemId) {
        if (!itemRepository.getItemById(itemId).getOwner().equals(userId)) {
            throw new PermissionException(
                    String.format("User with id %d does not have permission for do this", userId)
            );
        }
    }
}
