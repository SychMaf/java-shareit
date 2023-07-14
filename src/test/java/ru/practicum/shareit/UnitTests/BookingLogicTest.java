package ru.practicum.shareit.UnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingDBRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDBRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDBRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class BookingLogicTest {
    @Mock
    UserDBRepository userRepository;
    @Mock
    ItemDBRepository itemRepository;
    @Mock
    BookingDBRepository bookingRepository;
    @InjectMocks
    BookingServiceImpl bookingService;
    private final User user = new User(
            1L,
            "John",
            "first@user.com"
    );
    private final User anotherUser = new User(
            2L,
            "NotJohn",
            "second@user.com"
    );
    private final UserDto userDto = new UserDto(
            1L,
            "John",
            "first@user.com"
    );
    private final Item item = new Item(
            2L,
            "Дрель",
            "Простая дрель",
            true,
            anotherUser,
            null
    );
    private final ItemDto itemDto = new ItemDto(
            2L,
            "Дрель",
            "Простая дрель",
            true,
            null
    );
    private final Booking booking = new Booking(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            item,
            user,
            BookingStatus.APPROVED
    );
    private final BookingDto testBookingDto = new BookingDto(
            1L,
            LocalDateTime.of(2023, 10, 10, 10, 10, 10),
            LocalDateTime.of(2030, 10, 10, 10, 10, 10),
            BookingStatus.APPROVED,
            2L,
            1L,
            userDto,
            itemDto
    );

    @Test
    void createBookingTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingDto getBookingDto = bookingService.createBooking(testBookingDto, 1L);
        testBookingDto.setStatus(BookingStatus.APPROVED);
        assertEquals(testBookingDto, getBookingDto);
    }

    @Test
    void createBookingWithNotAvailableItemTest() {
        item.setAvailable(false);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        ItemNotAvailableException ex = Assertions.assertThrows(
                ItemNotAvailableException.class, () -> bookingService.createBooking(testBookingDto, 1L));
        assertEquals(String.format("Item with id %d does not have permission for booking", item.getId()), ex.getMessage());
        item.setAvailable(true);
    }

    @Test
    void changeBookingStatusTest() {
        booking.setStatus(BookingStatus.WAITING);
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito
                .when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingDto getBookingDto = bookingService.changeBookingStatus(2L, 1L, "true");
        assertEquals(testBookingDto, getBookingDto);
        booking.setStatus(BookingStatus.APPROVED);
    }

    @Test
    void getBookingInfoByIdTest() {
        Mockito
                .when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        BookingDto getBookingDto = bookingService.getBookingInfoById(1L, 1L);
        assertEquals(testBookingDto, getBookingDto);
    }

    @Test
    void getUserBookingListTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(
                        any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> getBookingDto = bookingService.getUserBookingList(1L, "FUTURE", 1, 1);
        assertThat(getBookingDto).hasSize(1).contains(testBookingDto);
    }

    @Test
    void getUserItemsBookingTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(anotherUser));
        Mockito
                .when(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                        any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> getBookingDto = bookingService.getUserItemsBooking(2L, "FUTURE", 1, 1);
        assertThat(getBookingDto).hasSize(1).contains(testBookingDto);
    }
}
