package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.GetState;
import ru.practicum.shareit.booking.repository.BookingDBRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.inDB.ItemDBRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inDB.UserDBRepository;
import ru.practicum.shareit.validator.BookingFieldsValidator;
import ru.practicum.shareit.validator.ItemFieldsValidator;
import ru.practicum.shareit.validator.UserFieldsValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDBRepository bookingRepository;
    private final ItemDBRepository itemRepository;
    private final UserDBRepository userRepository;

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
        BookingFieldsValidator.checkBookingTime(bookingDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with id %d does not exist"));
        ItemFieldsValidator.checkItemAvailable(item);
        BookingFieldsValidator.checkOwnerToBooking(item, userId);
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = bookingRepository.save(BookingDtoMapper.toBooking(bookingDto, item, user));
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto changeBookingStatus(Long userId, Long bookingId, String approved) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id %d does not exist"));
        ItemFieldsValidator.checkItemDoesntExist(itemRepository, booking.getItem().getId());
        BookingFieldsValidator.checkBookingToOwner(booking, userId);
        BookingFieldsValidator.checkBookingApproveStatus(booking);
        switch (approved.toUpperCase()) {
            case "TRUE":
                booking.setStatus(BookingStatus.APPROVED);
                break;
            case "FALSE":
                booking.setStatus(BookingStatus.REJECTED);
                break;
        }
        Booking patchBooking = bookingRepository.save(booking);
        return BookingDtoMapper.toBookingDto(patchBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingInfoById(Long userId, Long bookingId) {
        UserFieldsValidator.checkUserDoesntExist(userRepository, userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id %d does not exist"));
        BookingFieldsValidator.checkBookingToOwnerOrBooker(booking, userId);
        return BookingDtoMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookingList(Long userId, String state) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        List<Booking> result = List.of();
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            GetState groupGetState = GetState.valueOf(state.toUpperCase());
            switch (groupGetState) {
                case PAST:
                    result = bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, currentTime);
                    break;
                case CURRENT:
                    result = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartAsc(userId, currentTime, currentTime);
                    break;
                case FUTURE:
                    result = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(booker, currentTime);
                    break;
                case WAITING:
                case REJECTED:
                    result = bookingRepository.findAllByBookerAndStatusOrderByStartDesc(booker, BookingStatus.valueOf(state.toUpperCase()));
                    break;
                case ALL:
                    result = bookingRepository.findAllByBookerOrderByStartDesc(booker);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return result.stream()
                .map(BookingDtoMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserItemsBooking(Long userId, String state) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d does not exist"));
        List<Booking> result = List.of();
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            GetState groupGetState = GetState.valueOf(state.toUpperCase());
            switch (groupGetState) {
                case PAST:
                    result = bookingRepository.findByItem_Owner_IdAndEndIsBefore(userId, currentTime);
                    break;
                case CURRENT:
                    result = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(userId, currentTime, currentTime);
                    break;
                case FUTURE:
                    result = bookingRepository.findAllByItem_OwnerAndStartIsAfter(booker, currentTime);
                    break;
                case WAITING:
                case REJECTED:
                    result = bookingRepository.findAllByItem_OwnerAndStatus(booker, BookingStatus.valueOf(state.toUpperCase()));
                    break;
                case ALL:
                    result = bookingRepository.findAllByItem_Owner(booker);
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return result.stream()
                .map(BookingDtoMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
