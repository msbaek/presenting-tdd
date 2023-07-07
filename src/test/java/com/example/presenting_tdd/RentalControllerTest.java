package com.example.presenting_tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

@SpringBootTest
class RentalControllerTest {
    @Autowired
    private MovieRepository movieRepository;
    private RentalController rentalController;

    @BeforeEach
    void setUp() {
        rentalController = spy(new RentalController(movieRepository));
    }

    @Test
    void addRental() {
        movieRepository.save(new Movie(MovieType.CHILDRENS, "childrensMovieTitle"));

        RentalRequest request = new RentalRequest("childrensMovieTitle", 3);

        RentalResponse response = rentalController.addRental(request);
        assertThat(response.frequentRentalPoints()).isEqualTo(1);
        assertThat(response.charge()).isEqualTo(1.5);
    }
}