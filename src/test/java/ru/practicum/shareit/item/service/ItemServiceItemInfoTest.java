package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.ItemInfo;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceItemInfoTest {

    private final ItemService itemService;
    private static ItemInfo[] itemsInfoTestData;

    @BeforeAll
    public static void initUsersTestData() {
        itemsInfoTestData = TestData.getItemsInfo();
    }

    @Test
    public void shouldReturnItemInfoByItem1ForUser2() {
        ItemInfo itemInfo = itemService.getItemInfo(1L, 2L);
        assertThat(itemInfo, notNullValue());
        assertThat(itemInfo.getItem(), equalTo(itemsInfoTestData[0].getItem()));
        assertThat(itemInfo.getLastBooking(), nullValue());
        assertThat(itemInfo.getNextBooking(), nullValue());
        assertThat(itemInfo.getComments(), empty());
    }

    @Test
    public void shouldReturnItemInfoByItem2ForUser1() {
        ItemInfo itemInfo = itemService.getItemInfo(2L, 1L);
        assertThat(itemInfo, notNullValue());
        assertThat(itemInfo.getItem(), equalTo(itemsInfoTestData[1].getItem()));
        assertThat(itemInfo.getLastBooking(), nullValue());
        assertThat(itemInfo.getNextBooking(), nullValue());
        assertThat(itemInfo.getComments(), containsInAnyOrder(itemsInfoTestData[1].getComments().toArray()));
    }

    @Test
    public void shouldReturnItemInfoByItem2ForUser2() {
        ItemInfo itemInfo = itemService.getItemInfo(2L, 2L);
        assertThat(itemInfo, notNullValue());
        assertThat(itemInfo.getItem(), equalTo(itemsInfoTestData[1].getItem()));
        assertThat(itemInfo.getLastBooking(), equalTo(itemsInfoTestData[1].getLastBooking()));
        assertThat(itemInfo.getNextBooking(), equalTo(itemsInfoTestData[1].getNextBooking()));
        assertThat(itemInfo.getComments(), containsInAnyOrder(itemsInfoTestData[1].getComments().toArray()));
    }

    @Test
    public void shouldReturnItemInfoForUser1() {
        Collection<ItemInfo> itemsInfo = itemService.getItemsInfo(2L, 0, 20);
        assertThat(itemsInfo, notNullValue());
        assertThat(itemsInfo, hasSize(1));

        ItemInfo itemInfo = itemsInfo.stream().findFirst().orElse(null);
        assertThat(itemInfo, notNullValue());
        assertThat(itemInfo.getItem(), equalTo(itemsInfoTestData[1].getItem()));
        assertThat(itemInfo.getLastBooking(), equalTo(itemsInfoTestData[1].getLastBooking()));
        assertThat(itemInfo.getNextBooking(), equalTo(itemsInfoTestData[1].getNextBooking()));
        assertThat(itemInfo.getComments(), containsInAnyOrder(itemsInfoTestData[1].getComments().toArray()));
    }

}
