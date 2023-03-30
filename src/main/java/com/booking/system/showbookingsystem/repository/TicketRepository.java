package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, String> {
}
