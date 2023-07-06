package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.ValidationGroups;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(ValidationGroups.Create.class)
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.trace("Got request to add new item: {}", itemDto);
        return itemService.saveItem(itemDto, userId);
    }

    @GetMapping
    public List<ItemDtoLong> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.trace("Got request to all user items: {}", userId);
        return itemService.getAllUserItem(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoLong getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable Long itemId) {
        log.trace("Got request to get item with id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidationGroups.Update.class)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable Long itemId,
                              @RequestBody @Valid ItemDto itemDto) {
        log.trace("Got request to update item: {}", itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text) {
        log.trace("Got request to search item by string query: {}", text);
        return itemService.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto writeCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Valid CommentDto commentDto) {
        log.trace("Got request to write comment to item: {}", userId);
        return itemService.writeCommentToItem(userId, itemId, commentDto);
    }
}