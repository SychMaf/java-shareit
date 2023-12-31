package ru.practicum.shareit.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.CommentWithoutBookingException;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class BookingFieldsValidator {
    public void checkBookingToOwner(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new PermissionException(
                    String.format("User with id %d does not have permission for do this", userId)
            );
        }
    }

    public void checkBookingToOwnerOrBooker(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new PermissionException(
                    String.format("User with id %d does not have permission for do this", userId)
            );
        }
    }

    public void checkBookingApproveStatus(Booking booking) {
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new CommentWithoutBookingException("this booking already approved");
        }
    }

    public void checkOwnerToBooking(Item item, Long ownerId) {
        if (item.getOwner().getId().equals(ownerId)) {
            throw new PermissionException("owner cant create booking for his item");
        }
    }
}
