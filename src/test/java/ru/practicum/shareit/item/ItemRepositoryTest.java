package ru.practicum.shareit.item;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.PageableGenerator;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void contextLoads() {
        assertNotNull(em);
    }

    @Test
    public void verify() {
        List<Item> items = itemRepository
                .findItemsByText("Test Item A", PageableGenerator.getPageable(0, 20));
        assertNotNull(items, "A value is expected, null is received");
        assertEquals(1, items.size(), "Invalid size of result list");
        assertEquals(1, items.get(0).getId(), "Invalid result of searching");
    }
}
