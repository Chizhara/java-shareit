package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long sec = 0;

    public Optional<Item> findById(long itemId) {
        log.debug("Called method findById of class ItemRepository with args: itemId = {};", itemId);
        return Optional.of(items.get(itemId));
    }

    public Collection<Item> getAll(long userId) {
        log.debug("Called method getAll of class ItemRepository with args: userId = {};", userId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Item> save(Item item) {
        log.debug("Called method save of class ItemRepository with args: item = {};", item);
        item.setId(++sec);
        items.put(item.getId(), item);
        return Optional.of(item);
    }

    public Optional<Item> update(Item item) {
        log.debug("Called method update of class ItemRepository with args: item = {};", item);
        return Optional.of(updateItemFields(item));
    }

    public Collection<Item> searchByValue(String searchedValue) {
        log.debug("Called method searchByValue of class ItemRepository with args: searchedValue = {};", searchedValue);
        String lowerSearchedValue = searchedValue.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable() && itemFieldsContainsValue(item, lowerSearchedValue))
                .collect(Collectors.toList());
    }

    private Item updateItemFields(Item item) {
        log.trace("Called method updateItemFields of class ItemService with args: item = {};", item);
        Item itemUpdated = items.get(item.getId());

        validateItemsByUser(itemUpdated, item);

        if (item.getName() != null) {
            itemUpdated.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpdated.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdated.setAvailable(item.getAvailable());
        }

        return itemUpdated;
    }

    private void validateItemsByUser(Item itemInternal, Item itemExternal) {
        log.trace("Called method validateItemsByUser of class ItemService " +
                "with args: itemInternal = {}; itemExternal = {};", itemInternal, itemExternal);
        if (!itemInternal.getOwner().equals(itemExternal.getOwner())) {
            throw new NotFoundException(itemExternal.getId());
        }
    }

    private boolean itemFieldsContainsValue(Item item, String value) {
        return item.getName().toLowerCase().contains(value) || item.getDescription().toLowerCase().contains(value);
    }

}
