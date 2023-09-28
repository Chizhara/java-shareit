package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class BookerMapper {

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        log.trace("Called method toBookingDto of class BookerMapper with args: booking = {};", booking);
        return BookingInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .item(ItemMapper.toItemDto(booking.getItem()))
                .build();
    }

    public static Collection<BookingInfoDto> toBookingInfoDto(Collection<Booking> bookings) {
        log.trace("Called method toBookingDto of class BookerMapper with args: bookings = {};", bookings);
        return bookings.stream().map(BookerMapper::toBookingInfoDto).collect(Collectors.toList());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        log.trace("Called method toBooking of class BookerMapper with args: bookingInfoDto = {};", bookingDto);
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(Item.builder().id(bookingDto.getItemId()).build())
                .build();
    }

}
