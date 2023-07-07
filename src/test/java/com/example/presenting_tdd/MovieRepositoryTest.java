package com.example.presenting_tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.example.presenting_tdd.AddRentalsAcceptanceTest.childrensMovieTitle;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {
    @Autowired private MovieRepository movieRepository;

    @Test
    void findByTitle() {
        Movie save = movieRepository.save(new Movie(MovieType.CHILDRENS, childrensMovieTitle));
        Movie byTitle = movieRepository.findByTitle(save.getTitle());
        assertThat(save.getId()).isEqualTo(byTitle.getId());
    }
}