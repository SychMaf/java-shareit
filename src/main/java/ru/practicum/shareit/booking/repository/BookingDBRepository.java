package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDBRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartAsc(Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime localDateTime);

    List<Booking> findByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItem_OwnerAndStartIsAfter(User owner, LocalDateTime end);

    List<Booking> findAllByItem_OwnerAndStatus(User owner, BookingStatus status);

    List<Booking> findAllByItem_Owner(User owner);

    List<Booking> findAllByBooker_IdAndItem_IdAndStatusAndEndBefore(Long bookerId, Long itemId, BookingStatus bookingStatus, LocalDateTime end);

    @Query(value = "select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.start < CURRENT_TIMESTAMP " +
            "and b.status = 'APPROVED' " +
            "order by b.end desc")
    List<Booking> lastOrderBooking(Long itemId);

    @Query(value = "select b from Booking b" +
            " where b.item.id = ?1 " +
            "and b.start > CURRENT_TIMESTAMP " +
            "and b.status = 'APPROVED' " +
            "order by b.start asc")
    List<Booking> nextOrderBooking(Long itemId);
}
