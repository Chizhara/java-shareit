package ru.practicum.shareit;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemInfo;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class TestData {

    @Getter
    private static final User[] users = {
            User.builder()
                    .id(1L)
                    .name("User A")
                    .email("mail@mail.ru")
                    .build(),
            User.builder()
                    .id(2L)
                    .name("User B")
                    .email("email@mail.ru")
                    .build()
    };

    @Getter
    private static final Item[] items = {
            Item.builder()
                    .id(1L)
                    .name("Item A")
                    .description("Test Item A")
                    .available(true)
                    .owner(users[0])
                    .build(),
            Item.builder()
                    .id(2L)
                    .name("Item B")
                    .description("Test Item B")
                    .available(false)
                    .owner(users[1])
                    .build()
    };

    @Getter
    private static final Booking[] bookings = {
            Booking.builder()
                    .id(1L)
                    .start(LocalDateTime.of(2023, 11, 12, 12, 12, 12))
                    .end(LocalDateTime.of(2023, 12, 12, 12, 12, 12))
                    .status(BookingStatus.WAITING)
                    .item(items[0])
                    .booker(users[1])
                    .build(),
            Booking.builder()
                    .id(2L)
                    .start(LocalDateTime.of(2023, 6, 12, 12, 12, 12))
                    .end(LocalDateTime.of(2023, 11, 12, 12, 12, 12))
                    .status(BookingStatus.REJECTED)
                    .item(items[1])
                    .booker(users[0])
                    .build(),
            Booking.builder()
                    .id(3L)
                    .start(LocalDateTime.of(2023, 10, 12, 12, 12, 12))
                    .end(LocalDateTime.of(2023, 11, 12, 12, 12, 12))
                    .status(BookingStatus.APPROVED)
                    .item(items[1])
                    .booker(users[0])
                    .build(),
            Booking.builder()
                    .id(4L)
                    .start(LocalDateTime.of(2024, 6, 12, 12, 12, 12))
                    .end(LocalDateTime.of(2024, 11, 12, 12, 12, 12))
                    .status(BookingStatus.WAITING)
                    .item(items[1])
                    .booker(users[0])
                    .build(),
            Booking.builder()
                    .id(5L)
                    .start(LocalDateTime.of(2022, 11, 12, 12, 12, 12))
                    .end(LocalDateTime.of(2023, 6, 12, 12, 12, 12))
                    .status(BookingStatus.APPROVED)
                    .item(items[1])
                    .booker(users[0])
                    .build()
    };

    @Getter
    private static final ItemRequest[] requests = {
            ItemRequest.builder()
                    .id(1L)
                    .description("Request A")
                    .created(LocalDateTime.of(2023, 9, 12, 12, 12, 12))
                    .requester(users[0])
                    .items(List.of(items[0]))
                    .build(),
            ItemRequest.builder()
                    .id(2L)
                    .description("Request B")
                    .created(LocalDateTime.of(2023, 9, 12, 9, 9, 9))
                    .requester(users[0])
                    .build(),
            ItemRequest.builder()
                    .id(3L)
                    .description("Request C")
                    .created(LocalDateTime.of(2023, 9, 12, 10, 10, 10))
                    .requester(users[1])
                    .build()
    };

    @Getter
    private static final Comment[] comments = {
            Comment.builder()
                    .id(1L)
                    .text("Comment A")
                    .created(LocalDateTime.of(2023, 9, 12, 12, 12, 12))
                    .author(users[0])
                    .item(items[1])
                    .build(),
            Comment.builder()
                    .id(2L)
                    .text("Comment B")
                    .created(LocalDateTime.of(2023, 9, 16, 12, 12, 12))
                    .author(users[0])
                    .item(items[1])
                    .build()
    };

    @Getter
    private static final ItemInfo[] itemsInfo = {
            ItemInfo.builder().item(items[0]).lastBooking(null).nextBooking(null).build(),
            ItemInfo.builder()
                    .item(items[1])
                    .lastBooking(bookings[4])
                    .nextBooking(bookings[2])
                    .comments(List.of(comments))
                    .build()
    };

}
