package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Booking;
import com.booking.system.showbookingsystem.entity.Ticket;
import com.booking.system.showbookingsystem.repository.BookingRepository;
import com.booking.system.showbookingsystem.utils.BookingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking getBookingByShowNumberAndBuyerPhone(long showNumber, String buyerPhone) {
        Optional<Booking> booking = bookingRepository.findBookingByShowNumberAndBuyerPhone(showNumber, buyerPhone);
        return booking.orElse(null);
    }

    public List<Booking> getBookingsByShowNum(long showNumber) {
        return bookingRepository.findBookingsByShowNumber(showNumber);
    }

    public List<Booking> getBookingsByBuyerPhone(String buyerPhone) {
        return bookingRepository.findBookingsByBuyerPhone(buyerPhone);
    }


    public boolean deleteTicketFromBooking(String bookingId, Ticket ticket) {
        boolean bookingDeleted = false;
        Optional<Booking> optionalBooking = bookingRepository.findBookingByBookingId(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            List<Ticket> ticketList = BookingUtils.removeSpecificTicketFromList(booking.getTicketList(), ticket);
            if (ticketList.isEmpty()) {
                bookingRepository.delete(booking);
                bookingDeleted = true;
            } else {
                booking.setTicketList(ticketList);
                bookingRepository.save(booking);
            }
        }
        return bookingDeleted;
    }

    public void create(Booking booking) {
        bookingRepository.save(booking);
    }
}
