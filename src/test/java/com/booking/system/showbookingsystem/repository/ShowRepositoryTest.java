package com.booking.system.showbookingsystem.repository;

import com.booking.system.showbookingsystem.entity.Show;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
class ShowRepositoryTest {

    @Autowired
    private ShowRepository showRepository;

    @Test
    void shouldReturnPresentShowOptional_whenAbleToFindShowByShowNumber() {
        Optional<Show> optionalShow = showRepository.findById(1L);

        assertTrue(optionalShow.isPresent());
        Show show = optionalShow.get();
        assertEquals(1L, show.getShowNumber());
        assertEquals(1, show.getRows());
        assertEquals(1, show.getSeatsPerRow());
        assertEquals(1, show.getCancellationTime());
    }

    @Test
    void shouldReturnNullShowOptional_whenAbleToFindShowByShowNumber() {
        Optional<Show> optionalShow = showRepository.findById(3L);

        assertTrue(optionalShow.isEmpty());
    }

    @Test
    void shouldSaveNewShow_whenRunningCreateMethod() {
        Show expected = new Show(3L, 1, 1, 1);
        showRepository.save(expected);

        Optional<Show> optionalShow = showRepository.findById(3L);
        assertTrue(optionalShow.isPresent());
        assertEquals(expected, optionalShow.get());
    }
}