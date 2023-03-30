package com.booking.system.showbookingsystem.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class Booking {

    @Id
    private String bookingId;
    @Column
    private Long showNumber;
    @Column
    private String buyerPhone;
    @Column
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ticket> ticketList;
    @Column
    private LocalDateTime cancellationDateTime;

    public Booking(String bookingId, Long showNumber, String buyerPhone, List<Ticket> ticketList, LocalDateTime cancellationDateTime) {
        this.bookingId = bookingId;
        this.showNumber = showNumber;
        this.buyerPhone = buyerPhone;
        this.ticketList = ticketList;
        this.cancellationDateTime = cancellationDateTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Long getShowNumber() {
        return showNumber;
    }

    public LocalDateTime getCancellationDateTime() {
        return cancellationDateTime;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId) && Objects.equals(showNumber, booking.showNumber) && Objects.equals(buyerPhone, booking.buyerPhone) && Objects.equals(ticketList, booking.ticketList) && Objects.equals(cancellationDateTime, booking.cancellationDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, showNumber, buyerPhone, ticketList, cancellationDateTime);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", showNumber=" + showNumber +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", ticketList=" + ticketList +
                '}';
    }
}
