package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {

    Optional<Booking> findBookingByShowNumberAndBuyerPhone(Long showNumber, String buyerPhone);

    List<Booking> findBookingsByShowNumber(Long showNumber);

    Optional<Booking> findBookingByBookingId(String bookingId);

    List<Booking> findBookingsByBuyerPhone(String buyerPhone);
}
