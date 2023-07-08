package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long userId);

    BookingDto changeBookingStatus(Long userId, Long bookingId, String approved);

    BookingDto getBookingInfoById(Long userId, Long bookingId);

    List<BookingDto> getUserBookingList(Long userId, String state);

    List<BookingDto> getUserItemsBooking(Long userId, String state);
}
