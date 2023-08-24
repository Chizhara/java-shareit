package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserService userService;
    private final ItemRepository itemRepository;

    public Item getItem(long itemId) {
        log.info("Called method getItem of class ItemService with args: itemId = {};", itemId);
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId));
    }

    public Collection<Item> getItems(long ownerId) {
        log.info("Called method getItems of class ItemService with args: ownerId = {};", ownerId);
        return itemRepository.getAll(ownerId);
    }

    public Item addItem(Item item, long userId) {
        log.info("Called method addItem of class ItemService with args: item = {};", item);
        item.setOwner(userService.getUser(userId));
        return itemRepository.save(item)
                .orElseThrow(() -> new NotFoundException(item));
    }

    public Item updateItem(Item item, long userId) {
        log.info("Called method updateItem of class ItemService with args: item = {}; userId = {};", item, userId);
        item.setOwner(userService.getUser(userId));
        return itemRepository.update(item)
                .orElseThrow(() -> new NotFoundException(item));
    }

    public Collection<Item> searchItems(String searchedValue) {
        log.info("Called method searchItem of class ItemService with args: searchedValue = {};", searchedValue);
        if (searchedValue.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByValue(searchedValue);
    }

}
