package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
public class ItemInfoDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingDto nextBooking;
    private BookingDto lastBooking;
    @Builder.Default
    private Collection<CommentDto> comments = new ArrayList<>();

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;
    }

}
