package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @Validated
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        log.trace("SERVER: request to add new booking: {}", bookingDto);
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        log.trace("SERVER: request to change booking status: {}", approved);
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfoById(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable Long bookingId) {
        log.trace("SERVER: request to get info about booking by id: {}", bookingId);
        return bookingService.getBookingInfoById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookingList(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("SERVER: request to get list user booking by id: {}", userId);
        return bookingService.getUserBookingList(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getUserItemsBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.trace("SERVER: request to get list user items status by id: {}", userId);
        return bookingService.getUserItemsBooking(userId, state, from, size);
    }
}
