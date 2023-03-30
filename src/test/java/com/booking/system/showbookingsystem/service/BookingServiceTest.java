package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Ticket;
import com.booking.system.showbookingsystem.platform.MenuInputter;
import com.booking.system.showbookingsystem.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class BookingServiceTest {

    @MockBean
    private MenuInputter menuInputter;

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Test
    public void shouldReturnEmptyList_whenNoBookingsAreFoundByShowNum() {
        assertEquals(0, bookingService.getBookingsByShowNum(1L).size());
    }

    @Test
    public void shouldReturnListContainingOneBooking_whenOneBookingIsFoundByShowNum() {
        Booking booking = new Booking("ABC", 1L, "12345678", List.of(new Ticket()), LocalDateTime.parse("2023-03-20T00:44:42.00"));
        when(bookingRepository.findBookingsByShowNumber(anyLong())).thenReturn(List.of(booking));

        List<Booking> actual = bookingService.getBookingsByShowNum(1L);

        assertEquals(1, actual.size());
        assertEquals(booking, actual.get(0));
    }


    @Test
    public void shouldReturnNull_whenNoBookingsAreFoundByShowNumAndBuyerPhone() {
        assertNull(bookingService.getBookingByShowNumberAndBuyerPhone(1L, "12345678"));
    }

    @Test
    public void shouldReturnBooking_whenOneBookingIsFoundByShowNumAndBuyerPhone() {
        Booking booking = new Booking("ABC", 1L, "12345678", List.of(new Ticket()), LocalDateTime.parse("2023-03-20T00:44:42.00"));
        when(bookingRepository.findBookingByShowNumberAndBuyerPhone(anyLong(), anyString())).thenReturn(Optional.of(booking));

        Booking actual = bookingService.getBookingByShowNumberAndBuyerPhone(1L, "12345678");

        assertEquals(booking, actual);
    }

    @Test
    public void shouldReturnEmptyList_whenNoBookingsAreFoundByBuyerPhone() {
        assertEquals(0, bookingService.getBookingsByBuyerPhone("12345678").size());
    }

    @Test
    public void shouldReturnListContainingOneBooking_whenOneBookingIsFoundByBuyerPhone() {
        Booking booking = new Booking("ABC", 1L, "12345678", List.of(new Ticket()), LocalDateTime.parse("2023-03-20T00:44:42.00"));
        when(bookingRepository.findBookingsByBuyerPhone(anyString())).thenReturn(List.of(booking));

        List<Booking> actual = bookingService.getBookingsByBuyerPhone("12345678");

        assertEquals(1, actual.size());
        assertEquals(booking, actual.get(0));
    }

    @Test
    public void shouldReturnTrue_whenBookingHasNoTicketsLeftAfterCancellingTicket() {
        Ticket ticket = new Ticket("ABCD", "ABCDEF", "A1");
        Booking booking = new Booking("ABCDEF", 1L, "12345678", new ArrayList<>(List.of(ticket)), LocalDateTime.parse("2023-03-20T00:44:42.00"));
        when(bookingRepository.findBookingByBookingId(anyString())).thenReturn(Optional.of(booking));

        boolean actual = bookingService.deleteTicketFromBooking("ABCDEF", ticket);
        assertTrue(actual);
    }

    @Test
    public void shouldReturnFalse_whenBookingHasTicketsLeftAfterCancellingTicket() {
        Ticket ticket = new Ticket("ABCD", "ABCDEF", "A1");
        Ticket ticket2 = new Ticket("ABCDE", "ABCDEF", "A2");
        Booking booking = new Booking("ABCDEF", 1L, "12345678", new ArrayList<>(List.of(ticket, ticket2)), LocalDateTime.parse("2023-03-20T00:44:42.00"));
        when(bookingRepository.findBookingByBookingId(anyString())).thenReturn(Optional.of(booking));

        boolean actual = bookingService.deleteTicketFromBooking("ABCDEF", ticket);
        assertFalse(actual);
    }
}