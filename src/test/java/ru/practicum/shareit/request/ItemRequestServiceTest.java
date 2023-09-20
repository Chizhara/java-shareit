package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceTest {

    private final EntityManager em;

    private final ItemRequestService itemRequestService;
    private static ItemRequest[] itemRequestsTestData;
    private static ItemRequest itemRequestTestData;

    @BeforeAll
    public static void initUsersTestData() {
        itemRequestsTestData = TestData.getRequests();
        itemRequestTestData = ItemRequest.builder()
                .id(4L)
                .description("Request D")
                .requester(TestData.getUsers()[1])
                .build();
    }

    @Test
    public void shouldReturnRequest1ById1() {
        ItemRequest itemRequest = itemRequestService.getItemRequest(1L);
        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest, equalTo(itemRequestsTestData[0]));
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGetRequestByInvalidId() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequests(-1L));
    }

    @Test
    public void shouldReturnRequest1ById1AndRequester() {
        ItemRequest itemRequest = itemRequestService.getItemRequest(1L, 1L);
        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest, equalTo(itemRequestsTestData[0]));
    }

    @Test
    public void shouldReturnCorrectRequestsListOfRequester1() {
        List<ItemRequest> itemRequests = itemRequestService.getItemRequests(1L);
        Object[] itemRequestsTestData = Arrays.stream(ItemRequestServiceTest.itemRequestsTestData)
                .filter(itemRequest -> itemRequest.getRequester().getId() == 1L)
                .sorted(Comparator.comparing(ItemRequest::getCreated))
                .toArray();
        assertThat(itemRequests, notNullValue());
        assertThat(itemRequests.size(), equalTo(2));
        assertThat(itemRequests, containsInRelativeOrder(itemRequestsTestData));
    }

    @Test
    public void shouldReturnCorrectAlienRequestListOfRequester2() {
        List<ItemRequest> itemRequests = itemRequestService.getAlienItemRequests(2L, 0, 20);
        Object[] itemRequestsTestData = Arrays.stream(ItemRequestServiceTest.itemRequestsTestData)
                .filter(itemRequest -> itemRequest.getRequester().getId() == 1L)
                .sorted(Comparator.comparing(ItemRequest::getCreated))
                .toArray();
        assertThat(itemRequests, notNullValue());
        assertThat(itemRequests.size(), equalTo(2));
        assertThat(itemRequests, containsInRelativeOrder(itemRequestsTestData));
    }

    @Test
    public void shouldSaveItemRequest() {
        itemRequestService.addItemRequest(itemRequestTestData, 2);
        LocalDateTime creationTime = LocalDateTime.now();

        TypedQuery<ItemRequest> query = em
                .createQuery("SELECT ir FROM ItemRequest ir WHERE description = :description", ItemRequest.class);
        ItemRequest itemRequest = query
                .setParameter("description", itemRequestTestData.getDescription())
                .getSingleResult();

        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getId(), equalTo(itemRequestTestData.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestTestData.getDescription()));
        assertThat(itemRequest.getRequester(), equalTo(itemRequestTestData.getRequester()));
        assertThat(itemRequest.getCreated().getYear(), equalTo(creationTime.getYear()));
        assertThat(itemRequest.getCreated().getMonth(), equalTo(creationTime.getMonth()));
        assertThat(itemRequest.getCreated().getDayOfMonth(), equalTo(creationTime.getDayOfMonth()));
        assertThat(itemRequest.getCreated().getHour(), equalTo(creationTime.getHour()));
        itemRequestTestData.setId(itemRequestTestData.getId() + 1L);
    }

}
