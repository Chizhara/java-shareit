package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.PageableGenerator;
import ru.practicum.shareit.TestData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    private static Booking[] bookingsTestData;

    @BeforeAll
    public static void initBookingsList() {
        bookingsTestData = TestData.getBookings();
    }

    @Test
    @DisplayName("Find booking by booker")
    public void shouldReturnCorrectDataSearchedById1AndBooker2() {
        Optional<Booking> bookingOptional = bookingRepository.findById(1, 2);
        assertThat(bookingOptional.isPresent(), equalTo(true));
        Booking booking = bookingOptional.get();
        assertThat(booking, equalTo(bookingsTestData[0]));
    }

    @Test
    @DisplayName("Add booking by owner")
    public void shouldReturnCorrectDataSearchedById1AndOwner1() {
        Optional<Booking> bookingOptional = bookingRepository.findById(1, 1);
        assertThat(bookingOptional.isPresent(), equalTo(true));
        Booking booking = bookingOptional.get();
        assertThat(booking, equalTo(bookingsTestData[0]));
    }

    @Test
    public void shouldReturnCorrectDataSearchedByBookerIdAndCurrent() {
        List<Booking> bookings = bookingRepository
                .getAllByBookerIdCurrentAndBetweenTimeOrderByStartDesc(
                        1,
                        LocalDateTime.now(),
                        PageableGenerator.getPageable(0, 20));
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectDataSearchedByOwnerIdAndCurrent() {
        List<Booking> bookings = bookingRepository
                .getAllByOwnerIdCurrentAndBetweenTimeOrderByStartDesc(
                        2,
                        LocalDateTime.now(),
                        PageableGenerator.getPageable(0, 20));
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(bookingsTestData[1]));
    }

    @Test
    public void shouldReturnCorrectDataSearchedLastByItemId() {
        List<Booking> bookings = bookingRepository
                .getAllByLastApprovedByItemId(
                        2,
                        LocalDateTime.now());
        assertThat(bookings, hasSize(1));
    }

    @Test
    public void shouldReturnCorrectDataSearchedNextByItemId() {
        List<Booking> bookings = bookingRepository
                .getAllByNextApprovedByItemId(
                        2,
                        LocalDateTime.now());
        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0), equalTo(bookingsTestData[2]));
    }
}
