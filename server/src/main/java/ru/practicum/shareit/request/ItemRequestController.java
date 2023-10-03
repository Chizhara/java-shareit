package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.request.ItemRequestMapper.*;

import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("requestId") Long requestId) {
        log.info("Called method getItem of class ItemController with args: userId = {};", userId);
        return toItemRequestDto(itemRequestService.getItemRequest(requestId, userId));
    }

    @GetMapping
    public Collection<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Called method getItemRequests of class ItemController with args: userId = {};", userId);
        return toItemRequestDto(itemRequestService.getItemRequests(userId));
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "20") Integer size) {
        log.info("Called method getAllItemRequests of class ItemController " +
                "with args: userId = {}, from = {}, size = {};", userId, from, size);
        return toItemRequestDto(itemRequestService.getAlienItemRequests(userId, from, size));
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}, itemRequestDto = {};", userId, itemRequestDto);
        return toItemRequestDto(itemRequestService.addItemRequest(toItemRequest(itemRequestDto), userId));
    }

}
