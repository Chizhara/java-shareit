package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableGenerator;
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
        userService.validateUserExistById(userId);
        return bookingRepository.findById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(bookingId));
    }

    public List<Booking> getBookingsByBookerIdAndSate(long bookerId, State state, int from, int size) {
        log.info("Called method getBookingsByBookerIdAndSate of class BookingService with args: " +
                "bookerId = {}, state = {};", bookerId, state);
        userService.validateUserExistById(bookerId);
        return getBookingsByBookerIdSateResolver(bookerId, state, PageableGenerator.getPageable(from, size));
    }

    public List<Booking> getBookingsByOwnerIdAndSate(long ownerId, State state, int from, int size) {
        log.info("Called method getBookingsByOwnerIdAndSate of class BookingService with args: " +
                "ownerId = {}, state = {};", ownerId, state);
        userService.validateUserExistById(ownerId);
        return getBookingsByOwnerIdSateResolver(ownerId, state, PageableGenerator.getPageable(from, size));
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

    private List<Booking> getBookingsByBookerIdSateResolver(long bookerId, State state, Pageable pageable) {
        switch (state) {
            case ALL:
                return bookingRepository.getAllByBookerIdOrderByStartDesc(bookerId, pageable);
            case CURRENT:
                return bookingRepository
                        .getAllByBookerIdCurrentAndBetweenTimeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository
                        .getAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case FUTURE:
                return bookingRepository
                        .getAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case WAITING:
                return bookingRepository
                        .getAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository
                        .getAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED, pageable);
            default:
                return List.of();
        }
    }

    private List<Booking> getBookingsByOwnerIdSateResolver(long bookerId, State state, Pageable pageable) {
        switch (state) {
            case ALL:
                return bookingRepository.getAllByItemOwnerIdOrderByStartDesc(bookerId, pageable);
            case CURRENT:
                return bookingRepository
                        .getAllByOwnerIdCurrentAndBetweenTimeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository
                        .getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case FUTURE:
                return bookingRepository
                        .getAllByItemOwnerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
            case WAITING:
                return bookingRepository
                        .getAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING, pageable);
            case REJECTED:
                return bookingRepository
                        .getAllByItemOwnerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED, pageable);
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

}
