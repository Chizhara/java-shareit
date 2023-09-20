package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.ItemInfo;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerItemInfoTest {

    @MockBean
    ItemService itemService;

    private ItemInfoDto itemInfoDtoTestData;

    @Autowired
    private MockMvc mvc;

    private ItemInfo itemInfoTestData;
    private List<ItemInfo> itemsInfoTestData;

    @BeforeEach
    public void init() {
        itemInfoTestData = TestData.getItemsInfo()[1];
        itemsInfoTestData = List.of(TestData.getItemsInfo());

        itemInfoDtoTestData = ItemInfoDto.builder()
                .id(itemInfoTestData.getItem().getId())
                .name(itemInfoTestData.getItem().getName())
                .description(itemInfoTestData.getItem().getDescription())
                .available(itemInfoTestData.getItem().getAvailable())
                .ownerId(itemInfoTestData.getItem().getOwner().getId())
                .comments(itemInfoTestData.getComments().stream()
                        .map(comment -> CommentDto.builder()
                                .id(comment.getId())
                                .text(comment.getText())
                                .created(comment.getCreated())
                                .authorName(comment.getAuthor().getName())
                                .build())
                        .collect(Collectors.toList()))
                .nextBooking(ItemInfoDto.BookingDto.builder()
                        .bookerId(itemInfoTestData.getNextBooking().getBooker().getId())
                        .id(itemInfoTestData.getNextBooking().getId())
                        .build())
                .lastBooking(ItemInfoDto.BookingDto.builder()
                        .bookerId(itemInfoTestData.getLastBooking().getBooker().getId())
                        .id(itemInfoTestData.getLastBooking().getId())
                        .build())
                .build();
    }

    @Test
    public void shouldGetItemInfo() throws Exception {
        when(itemService.getItemInfo(itemInfoTestData.getItem().getId(), itemInfoTestData.getItem().getOwner().getId()))
                .thenReturn(itemInfoTestData);

        mvc.perform(get("/items/" + itemInfoTestData.getItem().getId())
                        .header("X-Sharer-User-Id", itemInfoTestData.getItem().getOwner().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInfoDtoTestData.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDtoTestData.getDescription())))
                .andExpect(jsonPath("$.available", is(itemInfoDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemInfoDtoTestData.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id",
                        is(itemInfoDtoTestData.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId",
                        is(itemInfoDtoTestData.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id",
                        is(itemInfoDtoTestData.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId",
                        is(itemInfoDtoTestData.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.comments.*", hasSize(itemInfoDtoTestData.getComments().size())));
    }

    @Test
    public void shouldGetItemsInfo() throws Exception {
        when(itemService.getItemsInfo(itemInfoTestData.getItem().getOwner().getId(), 0, 20))
                .thenReturn(itemsInfoTestData);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", itemInfoTestData.getItem().getOwner().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(itemsInfoTestData.size())))
                .andExpect(jsonPath("$.[1].id", is(itemInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemInfoDtoTestData.getName())))
                .andExpect(jsonPath("$.[1].description", is(itemInfoDtoTestData.getDescription())))
                .andExpect(jsonPath("$.[1].available", is(itemInfoDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.[1].ownerId", is(itemInfoDtoTestData.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.[1].nextBooking.id",
                        is(itemInfoDtoTestData.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.[1].nextBooking.bookerId",
                        is(itemInfoDtoTestData.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.[1].lastBooking.id",
                        is(itemInfoDtoTestData.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.[1].lastBooking.bookerId",
                        is(itemInfoDtoTestData.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.[1].comments.*", hasSize(itemInfoDtoTestData.getComments().size())));
    }
}
