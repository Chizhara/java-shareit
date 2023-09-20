package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceTest {

    private final EntityManager em;

    private final ItemService itemService;
    private static Item[] itemsTestData;
    private static Item itemTestData;

    @BeforeAll
    public static void initUsersTestData() {
        itemsTestData = TestData.getItems();
        itemTestData = Item.builder()
                .id(3L)
                .name("Item C")
                .description("Test Item C")
                .available(false)
                .owner(TestData.getUsers()[0])
                .request(TestData.getRequests()[0])
                .build();
    }

    @Test
    public void shouldReturnItem1ById1() {
        Item item = itemService.getItem(1L);
        assertThat(item, notNullValue());
        assertThat(item, equalTo(itemsTestData[0]));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetItemByInvalidId() {
        assertThrows(NotFoundException.class, () -> itemService.getItem(-1));
    }

    @Test
    public void shouldSaveItem() {
        itemService.addItem(itemTestData, 1L, 1L);

        TypedQuery<Item> query = em
                .createQuery("SELECT i FROM Item i WHERE id = :id", Item.class);
        Item item = query
                .setParameter("id", itemTestData.getId())
                .getSingleResult();

        assertThat(item, notNullValue());
        assertThat(item.getId(), equalTo(item.getId()));
        assertThat(item.getDescription(), equalTo(item.getDescription()));
        assertThat(item.getAvailable(), equalTo(item.getAvailable()));
        assertThat(item.getOwner(), equalTo(item.getOwner()));
        assertThat(item.getRequest(), equalTo(item.getRequest()));

        itemTestData.setId(itemTestData.getId() + 1L);
    }

    @Test
    public void shouldUpdateItemFields() {
        itemService.addItem(itemTestData, 1L, 1L);
        itemTestData.setName("Updated Item C");
        itemTestData.setAvailable(!itemTestData.getAvailable());
        itemTestData.setDescription("Updated Test Item C");
        itemService.updateItem(itemTestData, 1L);


        TypedQuery<Item> query = em
                .createQuery("SELECT i FROM Item i WHERE id = :id", Item.class);
        Item item = query
                .setParameter("id", itemTestData.getId())
                .getSingleResult();

        assertThat(item, notNullValue());
        assertThat(item.getId(), equalTo(item.getId()));
        assertThat(item.getDescription(), equalTo(item.getDescription()));
        assertThat(item.getAvailable(), equalTo(item.getAvailable()));
        assertThat(item.getOwner(), equalTo(item.getOwner()));
        assertThat(item.getRequest(), equalTo(item.getRequest()));

        item.setId(item.getId() + 1L);
    }

    @Test
    public void shouldReturnItem1SearchedByName() {
        Collection<Item> items = itemService.searchItems(itemsTestData[0].getName(), 0, 20);
        assertThat(items, notNullValue());
        assertThat(items.size(), equalTo(1));
        assertThat(items, contains(itemsTestData[0]));
    }

    @Test
    public void shouldReturnSearchedEmptyList() {
        Collection<Item> items = itemService.searchItems(" ", 0, 20);
        assertThat(items, notNullValue());
        assertThat(items, empty());
    }

}
