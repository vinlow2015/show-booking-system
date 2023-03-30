package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Ticket;
import com.booking.system.showbookingsystem.platform.MenuInputter;
import com.booking.system.showbookingsystem.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class TicketServiceTest {
    @MockBean
    private MenuInputter menuInputter;

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;


    @Test
    public void shouldReturnNull_whenNoTicketsAreFoundByTicketId() {
        assertNull(ticketService.getTicketById("ABCDE"));
    }

    @Test
    public void shouldReturnTicket_whenOneTicketsIsFoundByTicketId() {
        Ticket ticket = new Ticket("ABCD", "ABCDEF", "A1");
        when(ticketRepository.findById(anyString())).thenReturn(Optional.of(ticket));

        Ticket actual = ticketService.getTicketById("ABCD");

        assertEquals(ticket, actual);
    }

}