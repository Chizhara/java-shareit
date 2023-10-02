package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableGenerator;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemInfo;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;

    public ItemInfo getItemInfo(long itemId, long userId) {
        log.info("Called method getItem of class ItemService with args: itemId = {};", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId));
        if (item.getOwner().getId() == userId) {
            return generateItemInfo(item);
        }
        return new ItemInfo(item, null, null, getComments(itemId));
    }

    public Item getItem(long itemId) {
        log.info("Called method getItem of class ItemService with args: itemId = {};", itemId);
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId));
    }

    public Collection<ItemInfo> getItemsInfo(long ownerId, int from, int size) {
        log.info("Called method getItems of class ItemService with args: ownerId = {};", ownerId);
        Collection<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId, PageableGenerator.getPageable(from, size));
        return items.stream()
                .map(this::generateItemInfo)
                .collect(Collectors.toList());
    }

    @Transactional
    public Item addItem(Item item, Long requestId, long userId) {
        log.info("Called method addItem of class ItemService with args: item = {};", item);
        item.setOwner(userService.getUser(userId));
        initItemRequestOfItem(item, requestId);
        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Item item, long userId) {
        log.info("Called method updateItem of class ItemService with args: item = {}; userId = {};", item, userId);
        Item itemUpdated = itemRepository.findItemByIdAndOwnerId(item.getId(), userId)
                .orElseThrow(() -> new NotFoundException(item.getId()));
        updateItemFields(itemUpdated, item);
        return itemRepository.save(itemUpdated);
    }

    public Collection<Item> searchItems(String searchedValue, int from, int size) {
        log.info("Called method searchItem of class ItemService with args: searchedValue = {};", searchedValue);
        if (searchedValue.isBlank()) {
            return List.of();
        }
        return itemRepository.findItemsByText(searchedValue, PageableGenerator.getPageable(from, size));
    }

    @Transactional
    public Comment addComment(Comment comment, long authorId, long itemId) {
        log.info("Called method addComment of class ItemService with args: " +
                "comment = {}, authorId = {}, itemId = {};", comment, authorId, itemId);
        comment.setAuthor(userService.getUser(authorId));
        comment.setItem(getItem(itemId));
        comment.setCreated(LocalDateTime.now());
        validateAuthorHaveBookings(authorId, itemId);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(long itemId) {
        log.info("Called method getComments of class ItemService with args: itemId = {};", itemId);
        return commentRepository.getAllByItemId(itemId);
    }

    private void updateItemFields(Item itemUpdated, Item item) {
        log.trace("Called method updateItemFields of class ItemService with args: item = {};", item);

        if (item.getName() != null) {
            itemUpdated.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpdated.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdated.setAvailable(item.getAvailable());
        }
    }

    private ItemInfo generateItemInfo(Item item) {
        return new ItemInfo(item, getItemLastBooking(item),
                getItemNextBooking(item),
                getComments(item.getId()));
    }

    private Booking getItemLastBooking(Item item) {
        return bookingRepository
                .getAllByLastApprovedByItemId(item.getId(), LocalDateTime.now())
                .stream()
                .findFirst()
                .orElse(null);
    }

    private Booking getItemNextBooking(Item item) {
        return bookingRepository
                .getAllByNextApprovedByItemId(item.getId(), LocalDateTime.now())
                .stream().findFirst()
                .orElse(null);
    }

    private void validateAuthorHaveBookings(long authorId, long itemId) {
        if (bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBefore(itemId, authorId,
                BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new NotAvailableException("Отсутствуют заказы для " + itemId + " от пользователя " + authorId);
        }
    }

    private void initItemRequestOfItem(Item item, Long requestId) {
        if (requestId != null) {
            item.setRequest(itemRequestService.getItemRequest(requestId));
        }
    }

}
