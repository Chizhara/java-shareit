package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.Booking;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {
    private Item item;
    private Booking lastBooking;
    private Booking nextBooking;
    @Builder.Default
    private Collection<Comment> comments = new ArrayList<>();
}
