package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Called method getItem of class ItemController with args: userId = {};", userId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId) {
        log.info("Called method getItemRequests of class ItemController with args: userId = {};", userId);
        return itemRequestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method getAllItemRequests of class ItemController " +
                "with args: userId = {}, from = {}, size = {};", userId, from, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                                 @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}, itemRequestDto = {};", userId, itemRequestDto);
        return itemRequestClient.addItemRequest(itemRequestDto, userId);
    }

}
