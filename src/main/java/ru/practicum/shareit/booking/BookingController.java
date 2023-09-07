package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Collection;

import static ru.practicum.shareit.booking.BookerMapper.toBooking;
import static ru.practicum.shareit.booking.BookerMapper.toBookingInfoDto;

@Slf4j
@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingInfoDto getBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                     @PathVariable("bookingId") Long bookingId) {
        log.info("Called method getBooking of class BookingController with args: " +
                "userId = {}; bookingId = {};", userId, bookingId);
        return toBookingInfoDto(bookingService.getBooking(bookingId, userId));
    }


    @GetMapping
    public Collection<BookingInfoDto> getBookingsByBooker(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") State state) {
        log.info("Called method getBookingsByBooker of class BookingController with args: " +
                "bookerId = {}; state = {};", bookerId, state);
        return toBookingInfoDto(bookingService.getBookingsByBookerIdAndSate(bookerId, state));
    }

    @GetMapping("/owner")
    public Collection<BookingInfoDto> getBookingsByOwner(
            @RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") State state) {
        log.info("Called method getBookingsByOwner of class BookingController with args: " +
                "bookerId = {}; state = {};", bookerId, state);
        return toBookingInfoDto(bookingService.getBookingsByOwnerIdAndSate(bookerId, state));
    }

    @PostMapping
    public BookingInfoDto addBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                     @RequestBody @Valid BookingDto bookingDto) {
        log.info("Called method addBooking of class BookingController with args: " +
                "bookingDto = {}; bookerId = {};", bookingDto, bookerId);
        return toBookingInfoDto(bookingService.addBooking(toBooking(bookingDto), bookerId));
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto updateBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                        @PathVariable("bookingId") Long bookingId,
                                        @RequestParam(value = "approved") Boolean approved) {
        log.info("Called method updateBooking of class BookingController with args: " +
                "ownerId = {}, approved = {}, bookingId = {};", ownerId, approved, bookingId);
        return toBookingInfoDto(bookingService.updateBooking(bookingId, ownerId, approved));
    }
}
