package ru.practicum.shareit.IntegrationTests;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingTest {
    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    void createBookingTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        User anotherTestUser = new User(
                null,
                "NotJohn",
                "second@user.com"
        );
        em.persist(anotherTestUser);
        em.flush();
        BookingDto testBookingDto = new BookingDto(
                null,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2030, 10, 10, 10, 10, 10),
                null,
                item.getId(),
                null,
                null,
                null
        );
        bookingService.createBooking(testBookingDto, anotherTestUser.getId());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b", Booking.class);
        List<Booking> getBookings = query.getResultList();
        assertEquals(1, getBookings.size());
        assertEquals(anotherTestUser.getId(), getBookings.get(0).getBooker().getId());
        assertEquals(item.getId(), getBookings.get(0).getItem().getId());
        assertEquals(BookingStatus.WAITING, getBookings.get(0).getStatus());
        assertEquals(LocalDateTime.of(2023, 10, 10, 10, 10, 10), getBookings.get(0).getStart());
        assertEquals(LocalDateTime.of(2030, 10, 10, 10, 10, 10), getBookings.get(0).getEnd());
        em.clear();
    }

    @Test
    void changeBookingStatusTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        User anotherTestUser = new User(
                null,
                "NotJohn",
                "second@user.com"
        );
        em.persist(anotherTestUser);
        em.flush();
        Booking booking = new Booking(
                null,
                LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                LocalDateTime.of(2030, 10, 10, 10, 10, 10),
                item,
                anotherTestUser,
                BookingStatus.WAITING
        );
        em.persist(booking);
        em.flush();
        BookingDto getBookingDto = bookingService.changeBookingStatus(testUser.getId(), booking.getId(), "false");
        assertEquals(anotherTestUser.getId(), getBookingDto.getBooker().getId());
        assertEquals(item.getId(), getBookingDto.getItem().getId());
        assertEquals(BookingStatus.REJECTED, getBookingDto.getStatus());
        assertEquals(LocalDateTime.of(2023, 10, 10, 10, 10, 10), getBookingDto.getStart());
        assertEquals(LocalDateTime.of(2030, 10, 10, 10, 10, 10), getBookingDto.getEnd());
    }

    @Test
    void getUserBookingListTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        User anotherTestUser = new User(
                null,
                "NotJohn",
                "second@user.com"
        );
        em.persist(anotherTestUser);
        em.flush();
        Booking booking = new Booking(
                null,
                LocalDateTime.of(2013, 10, 10, 10, 10, 10),
                LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                item,
                anotherTestUser,
                BookingStatus.APPROVED
        );
        em.persist(booking);
        em.flush();
        List<BookingDto> getBookings = bookingService.getUserBookingList(anotherTestUser.getId(), "PAST", 0, 5);
        assertEquals(1, getBookings.size());
        assertEquals(anotherTestUser.getId(), getBookings.get(0).getBooker().getId());
        assertEquals(item.getId(), getBookings.get(0).getItem().getId());
        assertEquals(BookingStatus.APPROVED, getBookings.get(0).getStatus());
        assertEquals(LocalDateTime.of(2013, 10, 10, 10, 10, 10), getBookings.get(0).getStart());
        assertEquals(LocalDateTime.of(2020, 10, 10, 10, 10, 10), getBookings.get(0).getEnd());
        assertTrue(bookingService.getUserBookingList(anotherTestUser.getId(), "CURRENT", 0, 5).isEmpty());
        assertTrue(bookingService.getUserBookingList(anotherTestUser.getId(), "future", 0, 5).isEmpty());
        assertTrue(bookingService.getUserBookingList(anotherTestUser.getId(), "Waiting", 0, 5).isEmpty());
        assertTrue(bookingService.getUserBookingList(anotherTestUser.getId(), "RejeCted", 0, 5).isEmpty());
        assertEquals(1, bookingService.getUserBookingList(anotherTestUser.getId(), "AlL", 0, 5).size());
        UnsupportedStateException ex = Assertions.assertThrows(
                UnsupportedStateException.class, () -> bookingService.getUserBookingList(
                        anotherTestUser.getId(), "UnsupState", 0, 5));
        assertEquals("Unknown state: UNSUPPORTED_STATUS", ex.getMessage());
        em.clear();
    }

    @Test
    void getUserItemsBookingTest() {
        User testUser = new User(
                null,
                "John",
                "first@user.com"
        );
        em.persist(testUser);
        em.flush();
        Item item = new Item(
                null,
                "Дрель",
                "Простая дрель",
                true,
                testUser,
                null
        );
        em.persist(item);
        em.flush();
        User anotherTestUser = new User(
                null,
                "NotJohn",
                "second@user.com"
        );
        em.persist(anotherTestUser);
        em.flush();
        Booking booking = new Booking(
                null,
                LocalDateTime.of(2013, 10, 10, 10, 10, 10),
                LocalDateTime.of(2020, 10, 10, 10, 10, 10),
                item,
                anotherTestUser,
                BookingStatus.APPROVED
        );
        em.persist(booking);
        em.flush();
        List<BookingDto> getBookings = bookingService.getUserItemsBooking(testUser.getId(), "PAST", 0, 5);
        assertEquals(1, getBookings.size());
        assertEquals(anotherTestUser.getId(), getBookings.get(0).getBooker().getId());
        assertEquals(item.getId(), getBookings.get(0).getItem().getId());
        assertEquals(BookingStatus.APPROVED, getBookings.get(0).getStatus());
        assertEquals(LocalDateTime.of(2013, 10, 10, 10, 10, 10), getBookings.get(0).getStart());
        assertEquals(LocalDateTime.of(2020, 10, 10, 10, 10, 10), getBookings.get(0).getEnd());
        assertTrue(bookingService.getUserItemsBooking(testUser.getId(), "CURRENT", 0, 5).isEmpty());
        assertTrue(bookingService.getUserItemsBooking(testUser.getId(), "future", 0, 5).isEmpty());
        assertTrue(bookingService.getUserItemsBooking(testUser.getId(), "Waiting", 0, 5).isEmpty());
        assertTrue(bookingService.getUserItemsBooking(testUser.getId(), "RejeCted", 0, 5).isEmpty());
        assertEquals(1, bookingService.getUserItemsBooking(testUser.getId(), "AlL", 0, 5).size());
        UnsupportedStateException ex = Assertions.assertThrows(
                UnsupportedStateException.class, () -> bookingService.getUserItemsBooking(
                        testUser.getId(), "UnsupState", 0, 5));
        assertEquals("Unknown state: UNSUPPORTED_STATUS", ex.getMessage());
        em.clear();
    }
}
