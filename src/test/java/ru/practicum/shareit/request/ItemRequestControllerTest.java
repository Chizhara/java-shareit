package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDtoTestData;
    private ItemRequestDto.ItemDto itemDtoTestData;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private ItemRequest itemRequestTestData;
    private List<ItemRequest> itemRequestsTestData;

    @BeforeEach
    public void init() {
        itemRequestTestData = TestData.getRequests()[0];
        itemRequestsTestData = List.of(TestData.getRequests()[1], TestData.getRequests()[0]);

        itemDtoTestData = ItemRequestDto.ItemDto.builder()
                .id(1L)
                .name("Item A")
                .description("Test Item A")
                .available(true)
                .requestId(1L)
                .build();

        itemRequestDtoTestData = ItemRequestDto.builder()
                .id(1L)
                .description("Request A")
                .created(LocalDateTime.of(2023, 9, 12, 12, 12, 12))
                .items(List.of(itemDtoTestData))
                .build();
    }

    @Test
    @DisplayName("Add request")
    public void shouldSaveNewRequest() throws Exception {
        when(itemRequestService.addItemRequest(any(), anyLong()))
                .thenReturn(itemRequestTestData);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoTestData.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDtoTestData.getCreated().toString())))
                .andExpect(jsonPath("$.items", hasSize(itemRequestDtoTestData.getItems().size())))
                .andExpect(jsonPath("$.items[0].id", is(itemDtoTestData.getId().intValue())))
                .andExpect(jsonPath("$.items[0].available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(itemDtoTestData.getRequestId().intValue())))
                .andExpect(jsonPath("$.items[0].name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$.items[0].description", is(itemDtoTestData.getDescription())));
    }

    @Test
    @DisplayName("Get request")
    public void shouldGetItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(1, 1))
                .thenReturn(itemRequestTestData);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoTestData.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDtoTestData.getCreated().toString())))
                .andExpect(jsonPath("$.items", hasSize(itemRequestDtoTestData.getItems().size())))
                .andExpect(jsonPath("$.items[0].id", is(itemDtoTestData.getId().intValue())))
                .andExpect(jsonPath("$.items[0].available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(itemDtoTestData.getRequestId().intValue())))
                .andExpect(jsonPath("$.items[0].name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$.items[0].description", is(itemDtoTestData.getDescription())));
    }

    @Test
    @DisplayName("Get alien requests")
    public void shouldGetAllAlienItemRequestsWithDefaultValues() throws Exception {
        when(itemRequestService.getAlienItemRequests(2, 0, 20))
                .thenReturn(itemRequestsTestData);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(itemRequestsTestData.size())))
                .andExpect(jsonPath("$[1].id", is(itemRequestDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequestDtoTestData.getDescription())))
                .andExpect(jsonPath("$[1].created", is(itemRequestDtoTestData.getCreated().toString())))
                .andExpect(jsonPath("$[1].items", hasSize(itemRequestDtoTestData.getItems().size())))
                .andExpect(jsonPath("$[1].items[0].id", is(itemDtoTestData.getId().intValue())))
                .andExpect(jsonPath("$[1].items[0].available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$[1].items[0].requestId", is(itemDtoTestData.getRequestId().intValue())))
                .andExpect(jsonPath("$[1].items[0].name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(itemDtoTestData.getDescription())));
    }

    @Test
    @DisplayName("Get requests")
    public void shouldGetItemRequests() throws Exception {
        when(itemRequestService.getItemRequests(1))
                .thenReturn(itemRequestsTestData);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(itemRequestsTestData.size())))
                .andExpect(jsonPath("$[1].id", is(itemRequestDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequestDtoTestData.getDescription())))
                .andExpect(jsonPath("$[1].created", is(itemRequestDtoTestData.getCreated().toString())))
                .andExpect(jsonPath("$[1].items", hasSize(itemRequestDtoTestData.getItems().size())))
                .andExpect(jsonPath("$[1].items[0].id", is(itemDtoTestData.getId().intValue())))
                .andExpect(jsonPath("$[1].items[0].available", is(itemDtoTestData.getAvailable())))
                .andExpect(jsonPath("$[1].items[0].requestId", is(itemDtoTestData.getRequestId().intValue())))
                .andExpect(jsonPath("$[1].items[0].name", is(itemDtoTestData.getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(itemDtoTestData.getDescription())));
    }
}
