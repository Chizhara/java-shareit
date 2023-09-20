package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.item.model.Comment;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerCommentTest {
    @MockBean
    ItemService itemService;

    private CommentDto commentDto;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private Comment commentTestData;

    @BeforeEach
    public void init() {
        commentTestData = TestData.getComments()[0];

        commentDto = CommentDto.builder()
                .id(commentTestData.getId())
                .text(commentTestData.getText())
                .authorName(commentTestData.getAuthor().getName())
                .created(commentTestData.getCreated())
                .build();
    }

    @Test
    public void shouldSaveNewComment() throws Exception {
        when(itemService.addComment(any(Comment.class),
                eq(commentTestData.getAuthor().getId()),
                eq(commentTestData.getItem().getId())))
                .thenReturn(commentTestData);

        mvc.perform(post("/items/" + commentTestData.getItem().getId() + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", commentTestData.getAuthor().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}
