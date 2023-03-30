package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Ticket;
import com.booking.system.showbookingsystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class TicketService {
    final
    TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket getTicketById(String ticketId) {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        return ticket.orElse(null);
    }
}
