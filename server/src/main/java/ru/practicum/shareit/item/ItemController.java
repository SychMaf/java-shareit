package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto) {
        log.trace("SERVER: request to add new item: {}", itemDto);
        return itemService.saveItem(itemDto, userId);
    }

    @GetMapping()
    public List<ItemDtoLong> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.trace("SERVER: request to all user items: {}", userId);
        return itemService.getAllUserItem(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoLong getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable Long itemId) {
        log.trace("SERVER: request to get item with id: {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.trace("SERVER: request to update item: {}", itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text,
                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.trace("SERVER: request to search item by string query: {}", text);
        return itemService.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto writeCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentDto commentDto) {
        log.trace("SERVER: request to write comment to item: {}", userId);
        return itemService.writeCommentToItem(userId, itemId, commentDto);
    }
}