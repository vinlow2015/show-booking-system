package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldReturnPresentTicketOptional_whenAbleToFindTicketByTicketNumber() {
        Optional<Ticket> optionalTicket = ticketRepository.findById("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");

        assertTrue(optionalTicket.isPresent());
        Ticket ticket = optionalTicket.get();
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", ticket.getTicketId());
        assertEquals("f8c3de3d-1fea-4d7c-a8b0-29f63cssss", ticket.getBookingId());
        assertEquals("A1", ticket.getSeatNumber());
    }

    @Test
    void shouldReturnNullTicketOptional_whenNotAbleToFindTicketByTicketNumber() {
        Optional<Ticket> optionalTicket = ticketRepository.findById("ABCDE");

        assertTrue(optionalTicket.isEmpty());
    }
}