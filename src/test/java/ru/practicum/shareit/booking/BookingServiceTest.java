package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.AlreadyChangedException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceTest {
    private final EntityManager em;

    private final BookingService bookingService;
    private static Booking[] bookingsTestData;
    private static Booking bookingTestData;
    private static Booking bookingTestDataInvalid;
    private static Long id;

    @BeforeAll
    public static void initUsersTestData() {
        bookingsTestData = TestData.getBookings();
        id = 6L;
        bookingTestData = Booking.builder()
                .id(id)
                .start(LocalDateTime.of(2024, 10, 12, 12, 12, 12))
                .end(LocalDateTime.of(2024, 11, 12, 12, 12, 12))
                .item(TestData.getItems()[0])
                .build();
        bookingTestDataInvalid = Booking.builder()
                .start(LocalDateTime.of(2024, 10, 12, 12, 12, 12))
                .end(LocalDateTime.of(2024, 11, 12, 12, 12, 12))
                .item(TestData.getItems()[1])
                .build();
    }

    @Test
    @DisplayName("Get booking by owner")
    public void shouldReturnBooking1ById1AndOwner() {
        Booking booking = bookingService.getBooking(1L, 1L);
        assertThat(booking, notNullValue());
        assertThat(booking, equalTo(bookingsTestData[0]));
    }

    @Test
    @DisplayName("Get booking by booker")
    public void shouldReturnBooking1ById1AndBooker() {
        Booking booking = bookingService.getBooking(1L, 2L);
        assertThat(booking, notNullValue());
        assertThat(booking, equalTo(bookingsTestData[0]));
    }

    @Test
    @DisplayName("Get booking: exception - Invalid booking Id")
    public void shouldThrowNotFoundExceptionWhenGetBookingByInvalidId() {
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(-1L, 1L));
    }

    @Test
    @DisplayName("Get booking: exception - Invalid user Id")
    public void shouldThrowNotFoundExceptionWhenGetBookingById1AndInvalidOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(1L, -1L));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndAll() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.ALL, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(4));
        assertThat(bookings, containsInRelativeOrder(
                bookingsTestData[3], bookingsTestData[2], bookingsTestData[1], bookingsTestData[4]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndCurrent() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.CURRENT, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndPast() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.PAST, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[4]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndFuture() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.FUTURE, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[3], bookingsTestData[2]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndRejected() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.REJECTED, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByBookerIdAndWaiting() {
        List<Booking> bookings = bookingService.getBookingsByBookerIdAndSate(1L, State.WAITING, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[3]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndAll() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.ALL, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(4));
        assertThat(bookings, containsInRelativeOrder(
                bookingsTestData[3], bookingsTestData[2], bookingsTestData[1], bookingsTestData[4]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndCurrent() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.CURRENT, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndPast() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.PAST, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[4]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndFuture() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.FUTURE, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[3], bookingsTestData[2]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndRejected() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.REJECTED, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectBookingCollectionByOwnerIdAndWaiting() {
        List<Booking> bookings = bookingService.getBookingsByOwnerIdAndSate(2L, State.WAITING, 0, 20);
        assertThat(bookings, notNullValue());
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings, containsInRelativeOrder(bookingsTestData[3]));
    }

    @Test
    @DisplayName("Add booking")
    public void shouldSaveBooking() {
        bookingService.addBooking(bookingTestData, 2L);
        bookingTestData.setId(id);

        TypedQuery<Booking> query = em
                .createQuery("SELECT i FROM Booking i WHERE id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", bookingTestData.getId())
                .getSingleResult();

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), equalTo(booking.getId()));
        assertThat(booking.getStatus(), equalTo(booking.getStatus()));
        assertThat(booking.getStart(), equalTo(booking.getStart()));
        assertThat(booking.getEnd(), equalTo(booking.getEnd()));
        assertThat(booking.getItem(), equalTo(booking.getItem()));
        assertThat(booking.getBooker(), equalTo(booking.getBooker()));
        id++;
    }

    @Test
    @DisplayName("Add booking - exception: invalid status")
    public void shouldThrowNotAvailableExceptionWhenSaveInvalidItemStatus() {
        assertThrows(NotAvailableException.class, () -> bookingService.addBooking(bookingTestDataInvalid, 1L));
    }

    @Test
    @DisplayName("Add booking - exception: invalid booker")
    public void shouldThrowNotFoundExceptionWhenSaveInvalidBookerId() {
        assertThrows(NotFoundException.class, () -> bookingService.addBooking(bookingTestData, 1L));
    }

    @Test
    @DisplayName("Update booking approve")
    public void shouldUpdateBookingWhenApprove() {
        bookingService.addBooking(bookingTestData, 2L);
        bookingTestData.setId(id);
        TypedQuery<Booking> query = em
                .createQuery("SELECT i FROM Booking i WHERE id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", bookingTestData.getId())
                .getSingleResult();

        bookingService.updateBooking(bookingTestData.getId(), 1L, true);

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), equalTo(booking.getId()));
        assertThat(booking.getStatus(), equalTo(BookingStatus.APPROVED));
        id++;
    }

    @Test
    @DisplayName("Update booking reject")
    public void shouldUpdateBookingWhenReject() {
        bookingService.addBooking(bookingTestData, 2L);
        bookingTestData.setId(id);
        TypedQuery<Booking> query = em
                .createQuery("SELECT i FROM Booking i WHERE id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", bookingTestData.getId())
                .getSingleResult();

        bookingService.updateBooking(bookingTestData.getId(), 1L, false);

        assertThat(booking, notNullValue());
        assertThat(booking.getId(), equalTo(booking.getId()));
        assertThat(booking.getStatus(), equalTo(BookingStatus.REJECTED));
        id++;
    }

    @Test
    @DisplayName("Update booking - exception: repeat update")
    public void shouldThrowAlreadyChangedExceptionWhenSaveInvalidBookerId() {
        bookingService.addBooking(bookingTestData, 2L);
        bookingTestData.setId(id);
        bookingService.updateBooking(bookingTestData.getId(), 1L, false);
        assertThrows(AlreadyChangedException.class,
                () -> bookingService.updateBooking(bookingTestData.getId(), 1L, true));
        id++;
    }


}
