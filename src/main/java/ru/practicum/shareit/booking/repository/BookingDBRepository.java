package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDBRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findAllByBookerOrderByStartDesc(User booker, Pageable pageable);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartAsc(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime localDateTime, Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User owner, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStatusOrderByStartDesc(User owner, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User owner, Pageable pageable);

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
