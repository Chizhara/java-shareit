package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.PageableGenerator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    public ItemRequest getItemRequest(long requestId, long requesterId) {
        log.info("Called method getItemRequest of class ItemRequestService with args: " +
                "requesterId = {}, requesterId = {};", requestId, requesterId);
        userService.validateUserExistById(requesterId);
        return getItemRequest(requestId);
    }

    public ItemRequest getItemRequest(long requestId) {
        log.info("Called method getItemRequest of class ItemRequestService with args: " +
                "requesterId = {};", requestId);
        return itemRequestRepository
                .findById(requestId).orElseThrow(() -> new NotFoundException(requestId));
    }

    public List<ItemRequest> getItemRequests(long requesterId) {
        log.info("Called method getItemRequests of class ItemRequestService with args: " +
                "requesterId = {};", requesterId);
        userService.validateUserExistById(requesterId);
        return itemRequestRepository.getAllByRequesterIdOrderByCreated(requesterId);
    }

    public List<ItemRequest> getAlienItemRequests(long requesterId, int from, int size) {
        log.info("Called method getAlienItemRequests of class ItemRequestService with args: " +
                "requesterId = {}, from = {}, size = {};", requesterId, from, size);
        userService.validateUserExistById(requesterId);
        return itemRequestRepository
                .getAllByRequesterIdNotOrderByCreated(requesterId, PageableGenerator.getPageable(from, size));
    }

    @Transactional
    public ItemRequest addItemRequest(ItemRequest itemRequest, long requesterId) {
        log.info("Called method addItemRequest of class ItemRequestService with args: " +
                "itemRequest = {}, requesterId = {};", itemRequest, requesterId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(userService.getUser(requesterId));
        return itemRequestRepository.save(itemRequest);
    }

}
