package ru.practicum.shareit.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.inDB.ItemDBRepository;

@UtilityClass
public class ItemFieldsValidator {
    public void checkItemDoesntExist(ItemDBRepository itemRepository, Long userId) {
        if (!itemRepository.existsById(userId)) {
            throw new NotFoundException(
                    String.format("Item with id %d does not exist", userId)
            );
        }
    }

    public void checkItemToUser(ItemDBRepository itemRepository, Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new PermissionException(
                    String.format("User with id %d does not have permission for do this", userId)
            );
        }
    }

    public void checkItemAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException(
                    String.format("Item with id %d does not have permission for booking", item.getId())
            );
        }
    }
}
