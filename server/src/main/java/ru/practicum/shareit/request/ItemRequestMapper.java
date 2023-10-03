package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        log.trace("Called method toItemRequestDto of class ItemRequestMapper " +
                "with args: itemRequest = {};", itemRequest);
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(toItemDto(itemRequest.getItems(), itemRequest.getId()))
                .build();
    }

    public static Collection<ItemRequestDto> toItemRequestDto(Collection<ItemRequest> itemRequests) {
        log.trace("Called method toItemRequestDto of class ItemRequestMapper " +
                "with args: itemRequests = {};", itemRequests);
        return itemRequests.stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        log.trace("Called method toItemRequest of class ItemRequestMapper " +
                "with args: itemRequestDto = {};", itemRequestDto);
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();
    }

    private static ItemRequestDto.ItemDto toItemDto(Item item, Long itemRequestId) {
        log.trace("Called method toItemDto of class ItemRequestMapper " +
                "with args: item = {};", item);
        return ItemRequestDto.ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(itemRequestId)
                .build();
    }

    private static Collection<ItemRequestDto.ItemDto> toItemDto(Collection<Item> items, Long itemRequestId) {
        log.trace("Called method toItemDto of class ItemRequestMapper " +
                "with args: items = {};", items);
        if (items != null) {
            return items.stream().map(Item -> toItemDto(Item, itemRequestId)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }

}
