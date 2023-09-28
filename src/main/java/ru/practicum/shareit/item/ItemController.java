package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.item.mapper.CommentMapper.*;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemInfoDto getItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                               @PathVariable Long itemId) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}; itemId = {};", userId, itemId);
        return toItemInfoDto(itemService.getItemInfo(itemId, userId));
    }

    @GetMapping
    public Collection<ItemInfoDto> getItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method getItem of class ItemController " +
                "with args: userId = {}, from = {}, size = {};", userId, from, size);
        return toItemInfoDto(itemService.getItemsInfo(userId, from, size));
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam(value = "text", defaultValue = "") String value,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Called method searchItems of class ItemController " +
                "with args: value = {}, from = {}, size = {};", value, from, size);
        return toItemDto(itemService.searchItems(value, from, size));
    }

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Called method addItem of class ItemController " +
                "with args: itemDto = {}, userId = {};", itemDto, userId);
        return toItemDto(itemService.addItem(toItem(itemDto), itemDto.getRequestId(), userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable Long itemId,
                              @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Called method updateItem of class ItemController with args: itemDto = {};", itemDto);
        itemDto.setId(itemId);
        return toItemDto(itemService.updateItem(toItem(itemDto), userId));
    }

    @PostMapping({"/{itemId}/comment"})
    public CommentDto addComment(@RequestBody @Valid CommentDto commentDto,
                                 @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Called method addComment of class ItemController with args: " +
                "itemId = {} ;commentDto = {}; userId = {};", itemId, commentDto, userId);
        return toCommentDto(itemService.addComment(toComment(commentDto), userId, itemId));
    }

}
