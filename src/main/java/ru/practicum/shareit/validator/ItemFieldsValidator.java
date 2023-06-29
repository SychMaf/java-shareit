package ru.practicum.shareit.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.repository.ItemRepository;

@UtilityClass
public class ItemFieldsValidator {

    public void checkItemToUser(ItemRepository itemRepository, Long userId, Long itemId) {
        if (!itemRepository.getItemById(itemId).getOwner().equals(userId)) {
            throw new PermissionException(
                    String.format("User with id %d does not have permission for do this", userId)
            );
        }
    }
}
