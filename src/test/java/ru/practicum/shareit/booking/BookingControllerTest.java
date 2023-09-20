package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    private BookingInfoDto bookingInfoDtoTestData;
    private BookingDto bookingDtoTestData;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private Booking bookingTestData;
    private List<Booking> bookingsTestData;

    @BeforeEach
    public void init() {
        bookingTestData = TestData.getBookings()[0];
        bookingsTestData = Arrays.stream(TestData.getBookings())
                .filter(booking -> booking.getItem().getOwner().getId() == 1L).collect(Collectors.toList());

        bookingDtoTestData = BookingDto.builder()
                .start(bookingTestData.getStart())
                .end(bookingTestData.getEnd())
                .itemId(bookingTestData.getItem().getId())
                .build();

        bookingInfoDtoTestData = BookingInfoDto.builder()
                .id(bookingTestData.getId())
                .start(bookingTestData.getStart())
                .end(bookingTestData.getEnd())
                .item(ItemDto.builder()
                        .id(bookingTestData.getItem().getId())
                        .requestId(bookingTestData.getItem().getRequest() == null ? null : bookingTestData.getItem().getRequest().getId())
                        .name(bookingTestData.getItem().getName())
                        .description(bookingTestData.getItem().getDescription())
                        .available(bookingTestData.getItem().getAvailable())
                        .build())
                .booker(UserDto.builder()
                        .id(bookingTestData.getBooker().getId())
                        .name(bookingTestData.getBooker().getName())
                        .email(bookingTestData.getBooker().getEmail())
                        .build())
                .status(bookingTestData.getStatus())
                .build();
    }

    @Test
    public void shouldSaveNewBooking() throws Exception {
        when(bookingService.addBooking(any(), eq(bookingTestData.getBooker().getId())))
                .thenReturn(bookingTestData);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoTestData))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", bookingTestData.getBooker().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInfoDtoTestData.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingInfoDtoTestData.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDtoTestData.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDtoTestData.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingInfoDtoTestData.getStatus().name())));
    }

    @Test
    public void shouldUpdateBooking() throws Exception {
        when(bookingService.updateBooking(eq(bookingTestData.getId()), eq(bookingTestData.getBooker().getId()), any()))
                .thenReturn(bookingTestData);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", bookingTestData.getBooker().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInfoDtoTestData.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingInfoDtoTestData.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDtoTestData.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDtoTestData.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingInfoDtoTestData.getStatus().name())));
    }

    @Test
    public void shouldGetBooking() throws Exception {
        when(bookingService.getBooking(eq(bookingTestData.getId()), eq(bookingTestData.getBooker().getId())))
                .thenReturn(bookingTestData);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", bookingTestData.getBooker().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInfoDtoTestData.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingInfoDtoTestData.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDtoTestData.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDtoTestData.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingInfoDtoTestData.getStatus().name())));
    }

    @Test
    public void shouldGetBookerBookings() throws Exception {
        final State state = State.CURRENT;
        when(bookingService.getBookingsByBookerIdAndSate(bookingTestData.getBooker().getId(), state, 0, 20))
                .thenReturn(List.of(bookingTestData));

        mvc.perform(get("/bookings?state=CURRENT")
                        .header("X-Sharer-User-Id", bookingTestData.getBooker().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingInfoDtoTestData.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingInfoDtoTestData.getStart().toString())))
                .andExpect(jsonPath("$.[0].end", is(bookingInfoDtoTestData.getEnd().toString())))
                .andExpect(jsonPath("$.[0].item.id", is(bookingInfoDtoTestData.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingInfoDtoTestData.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(bookingInfoDtoTestData.getStatus().name())));
    }

    @Test
    public void shouldGetOwnerBookings() throws Exception {
        final State state = State.CURRENT;
        when(bookingService.getBookingsByOwnerIdAndSate(1L, state, 0, 20))
                .thenReturn(bookingsTestData);

        mvc.perform(get("/bookings/owner?state=CURRENT")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(bookingsTestData.size())));
    }
}
