package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ValidationGroups;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @Validated(ValidationGroups.Create.class)
    public ResponseEntity<Object> addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.trace("GATEWAY: request to add new item: {}", itemDto);
        return itemClient.saveItem(itemDto, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("GATEWAY: request to all user items: {}", userId);
        return itemClient.getAllUserItem(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable Long itemId) {
        log.trace("GATEWAY: request to get item with id: {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidationGroups.Update.class)
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.trace("GATEWAY: request to update item: {}", itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam String text,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("GATEWAY: request to search item by string query: {}", text);
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> writeCommentToItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable Long itemId,
                                                     @RequestBody @Valid CommentDto commentDto) {
        log.trace("GATEWAY: request to write comment to item: {}", userId);
        return itemClient.writeCommentToItem(userId, itemId, commentDto);
    }
}