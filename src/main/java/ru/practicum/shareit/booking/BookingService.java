package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyChangedException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public Booking getBooking(long bookingId, long userId) {
        log.info("Called method getBooking of class BookingService with args: " +
                "bookingId = {}, userId = {};", bookingId, userId);
        return bookingRepository.findById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(bookingId));
    }

    public List<Booking> getBookingsByBookerIdAndSate(long bookerId, State state) {
        log.info("Called method getBookingsByBookerIdAndSate of class BookingService with args: " +
                "bookerId = {}, state = {};", bookerId, state);
        validateUserIsExists(bookerId);
        return getBookingsByBookerIdSateResolver(bookerId, state);
    }

    public List<Booking> getBookingsByOwnerIdAndSate(long ownerId, State state) {
        log.info("Called method getBookingsByOwnerIdAndSate of class BookingService with args: " +
                "ownerId = {}, state = {};", ownerId, state);
        validateUserIsExists(ownerId);
        return getBookingsByOwnerIdSateResolver(ownerId, state);
    }

    @Transactional
    public Booking addBooking(Booking booking, Long bookerId) {
        log.info("Called method addBooking of class BookingService with args: " +
                "booking = {}, bookerId = {};", booking, bookerId);

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(userService.getUser(bookerId));
        booking.setItem(itemService.getItem(booking.getItem().getId()));
        validateBooking(booking);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(Long bookingId, Long ownerId, Boolean approved) {
        log.info("Called method updateBooking of class BookingService with args: " +
                "bookingId = {}, ownerId = {}, approved = {};", bookingId, ownerId, approved);
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new NotFoundException(bookingId));

        updateBookingStatus(booking, approved);
        return bookingRepository.save(booking);
    }

    private List<Booking> getBookingsByBookerIdSateResolver(long bookerId, State state) {
        switch (state) {
            case ALL:
                return bookingRepository.getAllByBookerIdOrderByStartDesc(bookerId);
            case CURRENT:
                return bookingRepository
                        .getAllByBookerIdCurrentAndStartAndEndBetweenOrderByStartDesc(bookerId, LocalDateTime.now());
            case PAST:
                return bookingRepository.getAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.getAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.getAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default:
                return List.of();
        }
    }

    private List<Booking> getBookingsByOwnerIdSateResolver(long bookerId, State state) {
        switch (state) {
            case ALL:
                return bookingRepository.getAllByItemOwnerIdOrderByStartDesc(bookerId);
            case CURRENT:
                return bookingRepository
                        .getAllByItemOwnerIdCurrentAndStartAndEndBetweenOrderByStartDesc(bookerId, LocalDateTime.now());
            case PAST:
                return bookingRepository.getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository
                        .getAllByItemOwnerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.getAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            default:
                return List.of();
        }
    }

    private void updateBookingStatus(Booking booking, boolean approved) {
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new AlreadyChangedException(booking);
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
    }

    private void validateBooking(Booking booking) {
        if (booking.getItem().getOwner().getId().equals(booking.getBooker().getId())) {
            throw new NotFoundException(booking.getBooker().getId());
        }
        if (!booking.getItem().getAvailable()) {
            throw new NotAvailableException(booking.getItem());
        }
    }

    private void validateUserIsExists(long userId) {
        if (!userService.isContainingUserWithId(userId)) {
            throw new NotFoundException(userId);
        }
    }

}
