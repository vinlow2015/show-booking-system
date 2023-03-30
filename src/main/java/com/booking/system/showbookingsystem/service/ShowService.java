package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Show;
import com.booking.system.showbookingsystem.repository.ShowRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    final
    ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Show getShowById(long id) {
        Optional<Show> show = showRepository.findById(id);
        return show.orElse(null);
    }

    public void create(Show show) {
        showRepository.save(show);
    }

    public List<Show> getAllShows() {
        List<Show> shows = new ArrayList<>();
        showRepository.findAll().forEach(shows::add);
        return shows;
    }
}
