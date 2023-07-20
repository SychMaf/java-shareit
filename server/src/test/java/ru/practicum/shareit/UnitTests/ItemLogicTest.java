package ru.practicum.shareit.UnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingDBRepository;
import ru.practicum.shareit.exception.CommentWithoutBookingException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLong;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentDBRepository;
import ru.practicum.shareit.item.repository.ItemDBRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDBRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class ItemLogicTest {
    @Mock
    UserDBRepository userRepository;
    @Mock
    ItemDBRepository itemRepository;
    @Mock
    CommentDBRepository commentRepository;
    @Mock
    BookingDBRepository bookingRepository;
    @InjectMocks
    ItemServiceImpl itemService;
    private final ItemDto itemDto = new ItemDto(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            null
    );
    private final User user = new User(
            1L,
            "John",
            "first@user.com"
    );
    private final UserDto userDto = new UserDto(
            1L,
            "John",
            "first@user.com"
    );
    private final Item item = new Item(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            user,
            null
    );
    private final CommentDto commentDto = new CommentDto(
            1L,
            "comment",
            "John",
            LocalDateTime.of(2023, 10, 10, 10, 10, 10)
    );
    private final Comment comment = new Comment(
            1L,
            "comment",
            user,
            item,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10)
    );
    private final Booking nextBooking = new Booking(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            item,
            user,
            BookingStatus.APPROVED
    );
    private final Booking lastBooking = new Booking(
            1L,
            LocalDateTime.of(2013, 10, 10, 10, 10, 10),
            LocalDateTime.of(2020, 10, 10, 10, 10, 10),
            item,
            user,
            BookingStatus.APPROVED
    );
    private final BookingDto lastBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2013, 10, 10, 10, 10, 10),
            LocalDateTime.of(2020, 10, 10, 10, 10, 10),
            BookingStatus.APPROVED,
            1L,
            1L,
            userDto,
            itemDto
    );
    private final BookingDto nextBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            BookingStatus.APPROVED,
            1L,
            1L,
            userDto,
            itemDto
    );

    private final ItemDtoLong itemDtoLong = new ItemDtoLong(
            1L,
            "Дрель",
            "Простая дрель",
            true,
            null,
            lastBookingDto,
            nextBookingDto,
            List.of(commentDto)
    );

    @Test
    void saveItemTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        ItemDto getItemDto = itemService.saveItem(itemDto, 1L);
        assertEquals(itemDto, getItemDto);
    }

    @Test
    void getItemByIdTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.nextOrderBooking(anyLong()))
                .thenReturn(List.of(nextBooking));
        Mockito
                .when(bookingRepository.lastOrderBooking(anyLong()))
                .thenReturn(List.of(lastBooking));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(commentRepository.findAllByItem(any(Item.class)))
                .thenReturn(List.of(comment));
        ItemDtoLong getItemDtoLong = itemService.getItemById(1L, 1L);
        assertEquals(itemDtoLong, getItemDtoLong);
    }

    @Test
    void updateItemTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        ItemDto getItemDto = itemService.updateItem(1L, 1L, itemDto);
        assertEquals(itemDto, getItemDto);
    }

    @Test
    void updateItemWithOtherUserTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        PermissionException ex = Assertions.assertThrows(
                PermissionException.class, () -> itemService.updateItem(99L, 1L, itemDto));
        assertEquals(String.format("User with id %d does not have permission for do this", 99), ex.getMessage());
    }

    @Test
    void getAllUserItemTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.findByOwner_Id(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));
        Mockito
                .when(bookingRepository.nextOrderBooking(anyLong()))
                .thenReturn(List.of(nextBooking));
        Mockito
                .when(bookingRepository.lastOrderBooking(anyLong()))
                .thenReturn(List.of(lastBooking));
        Mockito
                .when(commentRepository.findAllByItem(any(Item.class)))
                .thenReturn(List.of(comment));
        List<ItemDtoLong> getItemDtoLong = itemService.getAllUserItem(1L, 1, 1);
        assertEquals(1, getItemDtoLong.size());
        assertEquals(itemDtoLong, getItemDtoLong.get(0));
    }

    @Test
    void searchItemsTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.search(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemDto> getItemDto = itemService.searchItems(1L, "text test", 1, 1);
        assertThat(getItemDto).hasSize(1).contains(itemDto);
    }

    @Test
    void searchItemsWithEmptyQueryTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(itemRepository.search(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));
        List<ItemDto> getItemDto = itemService.searchItems(1L, "", 1, 1);
        assertThat(getItemDto).hasSize(0);
    }

    @Test
    void writeCommentToItemTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository
                        .findAllByBooker_IdAndItem_IdAndStatusAndEndBefore(
                                anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of(nextBooking));
        Mockito
                .when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        CommentDto getCommentDto = itemService.writeCommentToItem(1L, 1L, commentDto);
        assertEquals(commentDto, getCommentDto);
    }

    @Test
    void writeCommentToItemWhenBookingNotExistTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository
                        .findAllByBooker_IdAndItem_IdAndStatusAndEndBefore(
                                anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        Mockito
                .when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        CommentWithoutBookingException ex = Assertions.assertThrows(
                CommentWithoutBookingException.class, () -> itemService.writeCommentToItem(1L, 1L, commentDto));
        assertEquals("Booking for comment not found", ex.getMessage());
    }
}
