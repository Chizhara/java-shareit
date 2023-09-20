package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceCommentTest {

    private final EntityManager em;

    private final ItemService itemService;
    private static Comment[] commentsTestData;
    private static Comment commentTestData;

    @BeforeAll
    public static void initCommentsTestData() {
        commentsTestData = TestData.getComments();

        commentTestData = Comment.builder()
                .id(3L)
                .created(LocalDateTime.now())
                .item(TestData.getItems()[1])
                .author(TestData.getUsers()[0])
                .text("Comment C")
                .build();
    }

    @Test
    public void shouldSaveComment() {
        itemService.addComment(commentTestData, 1L, 2L);

        TypedQuery<Comment> query = em
                .createQuery("SELECT c FROM Comment c WHERE id = :id", Comment.class);
        Comment comment = query
                .setParameter("id", commentTestData.getId())
                .getSingleResult();

        assertThat(comment, notNullValue());
        assertThat(comment.getId(), equalTo(commentTestData.getId()));
        assertThat(comment.getText(), equalTo(commentTestData.getText()));
        assertThat(comment.getAuthor(), equalTo(commentTestData.getAuthor()));
        assertThat(comment.getItem(), equalTo(commentTestData.getItem()));

    }

    @Test
    public void shouldThrowNotAvailableExceptionComment() {
        assertThrows(NotAvailableException.class, () -> itemService.addComment(commentTestData, 2L, 1L));
    }

    @Test
    public void shouldReturnCorrectCommentsByItemId1() {
        Collection<Comment> comments = itemService.getComments(2L);
        assertThat(comments, notNullValue());
        assertThat(comments.size(), equalTo(2));
        assertThat(comments, containsInAnyOrder(commentsTestData));
    }

}
