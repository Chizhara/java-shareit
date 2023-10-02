package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingService;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        log.info("Called method getBooking of class BookingController with args: " +
                "userId = {}; bookingId = {};", userId, bookingId);
        return bookingService.getBooking(bookingId, userId);
    }


    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method getBookingsByBooker of class BookingController with args: " +
                "bookerId = {}; state = {}, from = {}, size = {};", bookerId, state, from, size);
        return bookingService.getBookingsByBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method getBookingsByOwner of class BookingController with args: " +
                "bookerId = {}; state = {}, from = {}, size = {};", bookerId, state, from, size);
        return bookingService.getBookingsByOwner(bookerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long bookerId,
                                             @RequestBody @Valid BookingDto bookingDto) {
        log.info("Called method addBooking of class BookingController with args: " +
                "bookingDto = {}; bookerId = {};", bookingDto, bookerId);
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long ownerId,
                                                @PathVariable("bookingId") Long bookingId,
                                                @RequestParam(value = "approved") Boolean approved) {
        log.info("Called method updateBooking of class BookingController with args: " +
                "ownerId = {}, approved = {}, bookingId = {};", ownerId, approved, bookingId);
        return bookingService.updateBooking(bookingId, ownerId, approved);
    }
}
