package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                          @PathVariable Long itemId) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}; itemId = {};", userId, itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}, from = {}, size = {};", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam(value = "text", required = true) String value,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method searchItems of class ItemController " +
                "with args: value = {}, from = {}, size = {};", value, from, size);
        return itemClient.searchItems(value, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDto itemDto,
                                          @RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId) {
        log.info("Called method addItem of class ItemController " +
                "with args: itemDto = {}, userId = {};", itemDto, userId);
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId,
                                             @RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId) {
        log.info("Called method updateItem of class ItemController with args: itemDto = {};", itemDto);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @PostMapping({"/{itemId}/comment"})
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto comment,
                                             @PathVariable Long itemId,
                                             @RequestHeader(ShareItGateway.USER_ID_HEADER_NAME) @NotNull Long userId) {
        log.info("Called method addComment of class ItemController with args: " +
                "itemId = {}; comment = {}; userId = {};", itemId, comment, userId);
        return itemClient.addComment(comment, itemId, userId);
    }

}
