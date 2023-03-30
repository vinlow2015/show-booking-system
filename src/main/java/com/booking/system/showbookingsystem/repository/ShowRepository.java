package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Show;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends CrudRepository<Show, Long> {
}
