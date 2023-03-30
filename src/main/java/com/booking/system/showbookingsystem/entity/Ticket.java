package com.booking.system.showbookingsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Ticket {

    @Id
    private String ticketId;
    @Column
    private String bookingId;
    @Column
    private String seatNumber;

    public String getTicketId() {
        return ticketId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getBookingId() {
        return bookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId) && Objects.equals(bookingId, ticket.bookingId) && Objects.equals(seatNumber, ticket.seatNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, bookingId, seatNumber);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
