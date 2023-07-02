package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.inDB.ItemDBRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;
import ru.practicum.shareit.validator.ItemFieldsValidator;
import ru.practicum.shareit.validator.UserFieldsValidator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDBRepository itemRepository;
    private final UserDBRepository userRepository;

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Item item = itemRepository.save(ItemDtoMapper.toItem(itemDto, user));
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, Long patchId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        ItemFieldsValidator.checkItemToUser(itemRepository, userId, patchId);
        Item patchItem = ItemDtoMapper.toItem(itemDto, user);
        if (patchItem.getName() != null) {
            itemRepository.updateName(patchId, patchItem.getName());
        }
        if (patchItem.getDescription() != null) {
            itemRepository.updateDescription(patchId, patchItem.getDescription());
        }
        if (patchItem.getAvailable() != null) {
            itemRepository.updateAvailable(patchId, patchItem.getAvailable());
        }

        return ItemDtoMapper.toItemDto(itemRepository.findById(patchId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist")));
    }

    @Override
    public List<ItemDto> getAllUserItem(long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        return itemRepository.findByOwner_Id(userId).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(long userId, String text) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
