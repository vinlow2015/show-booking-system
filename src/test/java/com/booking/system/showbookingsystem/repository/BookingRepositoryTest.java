package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldReturnPresentBookingOptional_whenAbleToFindBookingByShowNumberAndBuyerPhone() {
        Optional<Booking> optionalBooking = bookingRepository.findBookingByShowNumberAndBuyerPhone(1L, "12345678");

        assertTrue(optionalBooking.isPresent());
        Booking booking = optionalBooking.get();
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", booking.getBookingId());
        assertEquals(
                1L, booking.getShowNumber());
        assertEquals("12345678", booking.getBuyerPhone());
        assertEquals("2025-03-25T12:26:34.438754", booking.getCancellationDateTime().toString());
    }

    @Test
    void shouldReturnNullBookingOptional_whenNotAbleToFindBookingByShowNumberAndBuyerPhone() {
        Optional<Booking> optionalBooking = bookingRepository.findBookingByShowNumberAndBuyerPhone(1L, "12345679");

        assertTrue(optionalBooking.isEmpty());
    }

    @Test
    void shouldReturnListContainingOneBooking_whenAbleToFindBookingsByShowNumber() {
        List<Booking> result = bookingRepository.findBookingsByShowNumber(1L);

        assertEquals(1, result.size());
        Booking booking = result.get(0);
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", booking.getBookingId());
        assertEquals(
                1L, booking.getShowNumber());
        assertEquals("12345678", booking.getBuyerPhone());
        assertEquals("2025-03-25T12:26:34.438754", booking.getCancellationDateTime().toString());
    }

    @Test
    void shouldReturnEmptyList_whenNotAbleToFindBookingsByShowNumber() {
        List<Booking> result = bookingRepository.findBookingsByShowNumber(3L);

        assertEquals(0, result.size());
    }

    @Test
    void shouldReturnPresentBookingOptional_whenAbleToFindBookingByBookingId() {
        Optional<Booking> optionalBooking = bookingRepository.findBookingByBookingId("f8c3de3d-1fea-4d7c-a8b0-29f63cssss");

        assertTrue(optionalBooking.isPresent());
        Booking booking = optionalBooking.get();
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", booking.getBookingId());
        assertEquals(
                1L, booking.getShowNumber());
        assertEquals("12345678", booking.getBuyerPhone());
        assertEquals("2025-03-25T12:26:34.438754", booking.getCancellationDateTime().toString());
    }

    @Test
    void shouldReturnNullBookingOptional_whenNotAbleToFindBookingByBookingId() {
        Optional<Booking> optionalBooking = bookingRepository.findBookingByBookingId("f8c3de3d-1fea-4d7c-a8b0-29f63abcd");

        assertTrue(optionalBooking.isEmpty());
    }


    @Test
    void findBookingsByBuyerPhone() {
        List<Booking> result = bookingRepository.findBookingsByBuyerPhone("12345678");

        assertEquals(1, result.size());
        Booking booking = result.get(0);
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", booking.getBookingId());
        assertEquals(
                1L, booking.getShowNumber());
        assertEquals("12345678", booking.getBuyerPhone());
        assertEquals("2025-03-25T12:26:34.438754", booking.getCancellationDateTime().toString());
    }

    @Test
    void shouldReturnEmptyList_whenNotAbleToFindBookingsByBuyerPhone() {
        List<Booking> result = bookingRepository.findBookingsByBuyerPhone("12345679");

        assertEquals(0, result.size());
    }

    @Test
    void shouldSaveNewBooking_whenRunningCreateMethod() {
        Ticket ticket = new Ticket("ABCD", "ABCDEF", "A1");
        Booking expected = new Booking("ABCDEF", 2L, "12345679", List.of(ticket), LocalDateTime.parse("2023-03-20T00:44:42.00"));

        bookingRepository.save(expected);

        Optional<Booking> optionalBooking = bookingRepository.findBookingByBookingId("ABCDEF");
        assertTrue(optionalBooking.isPresent());
        assertEquals(expected, optionalBooking.get());
    }

    @Test
    void shouldDeleteBooking_whenRunningDeleteMethod() {
        Optional<Booking> optionalBooking = bookingRepository.findBookingByBookingId("f8c3de3d-1fea-4d7c-a8b0-29f63cssss");

        assertTrue(optionalBooking.isPresent());
        Booking booking = optionalBooking.get();
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", booking.getBookingId());
        assertEquals(
                1L, booking.getShowNumber());
        assertEquals("12345678", booking.getBuyerPhone());
        assertEquals("2025-03-25T12:26:34.438754", booking.getCancellationDateTime().toString());

        bookingRepository.delete(booking);

        optionalBooking = bookingRepository.findBookingByBookingId("f8c3de3d-1fea-4d7c-a8b0-29f63cssss");
        assertTrue(optionalBooking.isEmpty());
    }


}