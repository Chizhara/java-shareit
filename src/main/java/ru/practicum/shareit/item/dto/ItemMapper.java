package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        log.trace("Called method toItemDto of class ItemMapper with args: item = {};", item);
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public static Collection<ItemDto> toItemDto(Collection<Item> items) {
        log.trace("Called method toItemDto of class ItemMapper with args: items = {};", items);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
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

    public static Collection<Item> toItem(Collection<ItemDto> itemDtos) {
        log.trace("Called method toItem of class ItemMapper with args: itemDtos = {};", itemDtos);
        return itemDtos.stream().map(ItemMapper::toItem).collect(Collectors.toList());
    }

}
