package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

public class ItemRequestMapper {
    public static ItemRequestDto toUserDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(UserMapper.toUserDto(itemRequest.getRequester()))
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toUser(ItemRequestDto itemRequest) {
        return ItemRequest.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(UserMapper.toUser(itemRequest.getRequester()))
                .created(itemRequest.getCreated())
                .build();
    }
}
