package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingDBRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.inDB.CommentDBRepository;
import ru.practicum.shareit.item.repository.inDB.ItemDBRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;
import ru.practicum.shareit.validator.ItemFieldsValidator;
import ru.practicum.shareit.validator.UserFieldsValidator;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDBRepository itemRepository;
    private final BookingDBRepository bookingRepository;
    private final UserDBRepository userRepository;
    private final CommentDBRepository commentRepository;

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Item item = itemRepository.save(ItemDtoMapper.toItem(itemDto, user));
        return ItemDtoMapper.toItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoLong getItemById(Long itemId, Long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        ItemDtoLong itemDtoLong = ItemDtoMapper.toItemDtoLong(item, commentRepository.findAllByItem(item));
        if (item.getOwner().getId().equals(userId)) {
            fillOrderBooking(itemDtoLong);
        }
        return itemDtoLong;
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, Long patchId, ItemDto itemDto) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        ItemFieldsValidator.checkItemToUser(itemRepository, userId, patchId);
        Item outdatedItem = itemRepository.findById(patchId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        Item updateItem = ItemDtoMapper.updateItem(itemDto, outdatedItem);
        Item patchItem = itemRepository.save(updateItem);
        return ItemDtoMapper.toItemDto(patchItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoLong> getAllUserItem(long userId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        return itemRepository.findByOwner_Id(userId).stream()
                .map(item -> ItemDtoMapper.toItemDtoLong(item, commentRepository.findAllByItem(item)))
                .map(this::fillOrderBooking)
                .sorted(Comparator.comparing(ItemDtoLong::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchItems(long userId, String text) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto writeCommentToItem(long userId, Long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        LocalDateTime current = LocalDateTime.now();
        List<Booking> checkBooking =
                bookingRepository.findAllByBooker_IdAndItem_IdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED, current);
        if (checkBooking.isEmpty()) {
            throw new ValidationException("Booking for comment not found");
        }
        Comment comment = CommentDtoMapper.toComment(
                commentDto,
                user,
                item,
                current
        );
        return CommentDtoMapper.toCommentDto(commentRepository.save(comment), user.getName());
    }

    private ItemDtoLong fillOrderBooking(ItemDtoLong itemDtoLong) {
        List<Booking> nextList = bookingRepository.nextOrderBooking(itemDtoLong.getId());
        List<Booking> lastList = bookingRepository.lastOrderBooking(itemDtoLong.getId());
        if (!nextList.isEmpty()) {
            BookingDto nextBooking = BookingDtoMapper.toBookingDto(nextList.get(0));
            itemDtoLong.setNextBooking(nextBooking);
        }
        if (!lastList.isEmpty()) {
            BookingDto lastBooking = BookingDtoMapper.toBookingDto(lastList.get(0));
            itemDtoLong.setLastBooking(lastBooking);
        }
        return itemDtoLong;
    }
}
