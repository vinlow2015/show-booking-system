package com.booking.system.showbookingsystem.service;

import com.booking.system.showbookingsystem.entity.Show;
import com.booking.system.showbookingsystem.platform.MenuInputter;
import com.booking.system.showbookingsystem.repository.ShowRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class ShowServiceTest {

    @MockBean
    private MenuInputter menuInputter;

    @InjectMocks
    private ShowService showService;

    @Mock
    private ShowRepository showRepository;

    @Test
    void shouldReturnNull_whenNoShowsAreFoundByShowId() {
        assertNull(showService.getShowById(3L));
    }

    @Test
    void shouldReturnShow_whenOneShowIsFoundByShowId() {
        Show expected = new Show(1L, 1, 1, 1);
        when(showRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        Show actual = showService.getShowById(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyList_whenNoShowsAreFound() {
        assertEquals(0, showService.getAllShows().size());
    }

    @Test
    void shouldReturnListContainingTwoShows_whenTwoShowsAreFound() {
        Show show1 = new Show(1L, 1, 1, 1);
        Show show2 = new Show(2L, 1, 1, 1);
        List<Show> expected = List.of(show1, show2);
        when(showRepository.findAll()).thenReturn(expected);

        List<Show> actual = showService.getAllShows();
        assertEquals(2, actual.size());
        assertEquals(expected, actual);


    }
}