package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerItemTest {
    @MockBean
    ItemService itemService;

    private ItemDto itemDtoTestData;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private Item itemTestData;
    private List<Item> itemsTestData;

    @BeforeEach
    public void init() {
        itemTestData = TestData.getItems()[0];
        itemsTestData = List.of(TestData.getItems()[1], TestData.getItems()[0]);
        itemTestData.setRequest(TestData.getRequests()[0]);

        itemDtoTestData = ItemDto.builder()
                .id(1L)
                .name("Item A")
                .description("Test Item A")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    @DisplayName("Add Item")
    public void shouldSaveNewItem() throws Exception {
        when(itemService.addItem(any(Item.class),
                eq(itemDtoTestData.getRequestId()),
                eq(itemTestData.getOwner().getId())))
                .thenReturn(itemTestData);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTestData.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDtoTestData.getRequestId()), Long.class));
    }

    @Test
    @DisplayName("Get item")
    public void shouldGetItemById() throws Exception {
        when(itemService.updateItem(any(Item.class), eq(1L)))
                .thenReturn(itemTestData);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTestData.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDtoTestData.getRequestId()), Long.class));
    }

    @Test
    @DisplayName("Search items")
    public void shouldSearchItemByText() throws Exception {
        when(itemService.searchItems("Item", 0, 20)).thenReturn(itemsTestData);

        mvc.perform(get("/items/search?text=Item")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[1].id", is(itemDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$.[1].description", is(itemDtoTestData.getDescription())))
                .andExpect(jsonPath("$.[1].available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.[1].requestId", is(itemDtoTestData.getRequestId()), Long.class));
    }

}
