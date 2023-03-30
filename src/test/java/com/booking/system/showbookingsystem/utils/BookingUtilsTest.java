package com.booking.system.showbookingsystem.utils;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Ticket;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BookingUtilsTest {

    @Test
    public void shouldReturnNull_whenEnteringInvalidLong() {
        Long result = BookingUtils.parseLong("test");
        assertNull(result);
    }

    @Test
    public void shouldReturnLong_whenEnteringValidLong() {
        Long result = BookingUtils.parseLong("7");
        assertEquals(result, 7L);
    }

    @Test
    public void shouldReturnNegativeInteger_whenEnteringInvalidIntegerWithConstraints() {
        int result = BookingUtils.parseIntWithConstraints("test", 27);
        assertEquals(result, -1);
    }

    @Test
    public void shouldReturnNegativeInteger_whenEnteringIntegerBeyondBoundsWithConstraints() {
        int result = BookingUtils.parseIntWithConstraints("28", 27);
        assertEquals(result, -1);
    }

    @Test
    public void shouldReturnInteger_whenEnteringValidIntegerWithinBoundsWithConstraints() {
        int result = BookingUtils.parseIntWithConstraints("26", 27);
        assertEquals(result, 26);
    }

    @Test
    public void shouldReturnNegativeInteger_whenEnteringIntegerBeyondBounds() {
        int result = BookingUtils.parseInt("0");
        assertEquals(result, -1);
    }

    @Test
    public void shouldReturnInteger_whenEnteringValidIntegerWithinBounds() {
        int result = BookingUtils.parseInt("5");
        assertEquals(result, 5);
    }

    @Test
    public void shouldReturnSeatsListContaining2Seats_whenGiven2RowAnd1Seat() {
        Map<String, List<String>> expected = generateDefaultSeatsMap();

        Map<String, List<String>> result = BookingUtils.generateSeats(2, 1, new ArrayList<>());

        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnNull_whenGiven2RowAnd1SeatAndNoAvailableSeats() {
        Ticket ticket = new Ticket("ABC", "ABC", "A1");
        Ticket ticket1 = new Ticket("ABD", "ABC", "B1");
        Booking booking = new Booking("ABC", 1L, "12345678",
                List.of(ticket, ticket1), LocalDateTime.parse("2023-03-20T00:44:42.00"));

        assertNull(BookingUtils.generateSeats(2, 1, List.of(booking)));
    }

    @Test
    public void shouldReturnFalse_whenSeatsContainInvalidRowLetter() {
        List<String> seatsToBook = List.of("C1");
        assertFalse(BookingUtils.checkIfSeatsToBookAreValid(generateDefaultSeatsMap(), seatsToBook));
    }

    @Test
    public void shouldReturnFalse_whenSeatsContainInvalidSeat() {
        List<String> seatsToBook = List.of("B2");
        assertFalse(BookingUtils.checkIfSeatsToBookAreValid(generateDefaultSeatsMap(), seatsToBook));
    }

    @Test
    public void shouldReturnTrue_whenSeatsContainValidSeat() {
        List<String> seatsToBook = List.of("B1");
        assertTrue(BookingUtils.checkIfSeatsToBookAreValid(generateDefaultSeatsMap(), seatsToBook));
    }

    @Test
    public void shouldReturnTrue_whenCurrentTimeIsAfterCancellationTime() {
        Clock clock = Clock.fixed(Instant.parse("2023-03-19T16:45:42.00Z"), ZoneId.of("Asia/Singapore"));
        LocalDateTime cancellationDateTime = LocalDateTime.parse("2023-03-19T16:44:42.00");

        assertTrue(BookingUtils.checkIfCurrentTimeIsAfterCancellationDateTime(clock, cancellationDateTime));

    }

    @Test
    public void shouldReturnFalse_whenCurrentTimeIsBeforeCancellationTime() {
        Clock clock = Clock.fixed(Instant.parse("2023-03-19T16:42:42.00Z"), ZoneId.of("Asia/Singapore"));
        LocalDateTime cancellationDateTime = LocalDateTime.parse("2023-03-19T16:44:42.00");

        assertTrue(BookingUtils.checkIfCurrentTimeIsAfterCancellationDateTime(clock, cancellationDateTime));
    }

    @Test
    public void shouldReturnDateTimePlusTwoMinutes_whenTwoMinutesIsGivenWithCurrentDateTime() {
        Clock clock = Clock.fixed(Instant.parse("2023-03-19T16:42:42.00Z"), ZoneId.of("Asia/Singapore"));
        LocalDateTime expected = LocalDateTime.parse("2023-03-20T00:44:42.00");

        assertEquals(expected, BookingUtils.getCancellationDateTime(clock, 2));
    }

    @Test
    public void shouldReturnFalse_whenGivenInvalidPhoneNumberWithOnly7Char() {
        String phoneNumber = "1234567";

        assertFalse(BookingUtils.checkIfValidPhoneNumber(phoneNumber));
    }

    @Test
    public void shouldReturnFalse_whenGivenInvalidPhoneNumberWithNonDigits() {
        String phoneNumber = "123456-8";

        assertFalse(BookingUtils.checkIfValidPhoneNumber(phoneNumber));
    }

    @Test
    public void shouldReturnTrue_whenGivenValidPhoneNumberWithOnlyDigits() {
        String phoneNumber = "12345678";

        assertTrue(BookingUtils.checkIfValidPhoneNumber(phoneNumber));
    }

    @Test
    public void shouldReturnNull_whenGivenNull() {
        assertNull(BookingUtils.parseCommaSeparatedString(null));
    }

    @Test
    public void shouldReturnListOfStrings_whenGivenValidCommaSeparatedString() {
        String testString = "A1,B2";

        assertEquals(List.of("A1", "B2"), BookingUtils.parseCommaSeparatedString(testString));
    }

    @Test
    public void shouldReturnEmptyList_whenGivenAListWithOneTicketAndMatchingTicket() {
        Ticket ticket = new Ticket("ABC", "ABC", "A1");
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);

        assertTrue(BookingUtils.removeSpecificTicketFromList(ticketList, ticket).isEmpty());
    }

    @Test
    public void shouldReturnListWithOneTicket_whenGivenAListWithTwoTicketAndMatchingTicket() {
        Ticket ticketToRemove = new Ticket("ABC", "ABC", "A1");
        Ticket ticketToKeep = new Ticket("ABD", "ABC", "A1");
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticketToRemove);
        ticketList.add(ticketToKeep);

        List<Ticket> actual = BookingUtils.removeSpecificTicketFromList(ticketList, ticketToRemove);

        assertEquals(1, actual.size());
        assertEquals(ticketToKeep, actual.get(0));
    }

    private Map<String, List<String>> generateDefaultSeatsMap() {
        Map<String, List<String>> seatsMap = new LinkedHashMap<>();
        seatsMap.put("A", List.of("A1"));
        seatsMap.put("B", List.of("B1"));
        return seatsMap;
    }


}