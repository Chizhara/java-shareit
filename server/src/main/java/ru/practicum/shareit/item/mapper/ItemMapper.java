package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemInfo;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        log.trace("Called method toItemDto of class ItemMapper with args: item = {};", item);
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .build();
    }

    public static Collection<ItemDto> toItemDto(Collection<Item> items) {
        log.trace("Called method toItemDto of class ItemMapper with args: items = {};", items);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static ItemInfoDto toItemInfoDto(ItemInfo itemInfo) {
        log.trace("Called method toItemDto of class ItemMapper with args: itemInfo = {};", itemInfo);
        return ItemInfoDto.builder()
                .id(itemInfo.getItem().getId())
                .name(itemInfo.getItem().getName())
                .description(itemInfo.getItem().getDescription())
                .available(itemInfo.getItem().getAvailable())
                .ownerId(itemInfo.getItem().getOwner().getId())
                .lastBooking(toItemInfoBookingDto(itemInfo.getLastBooking()))
                .nextBooking(toItemInfoBookingDto(itemInfo.getNextBooking()))
                .comments(CommentMapper.toCommentDto(itemInfo.getComments()))
                .build();
    }

    public static Collection<ItemInfoDto> toItemInfoDto(Collection<ItemInfo> items) {
        log.trace("Called method toItemDto of class ItemMapper with args: items = {};", items);
        return items.stream().map(ItemMapper::toItemInfoDto).collect(Collectors.toList());
    }


    public static Item toItem(ItemDto itemDto) {
        log.trace("Called method toItem of class ItemMapper with args: itemDto = {};", itemDto);
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    private static ItemInfoDto.BookingDto toItemInfoBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return ItemInfoDto.BookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

}
