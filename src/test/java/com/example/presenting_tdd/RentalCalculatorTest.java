package com.example.presenting_tdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.example.presenting_tdd.AddRentalsAcceptanceTest.*;
import static com.example.presenting_tdd.MovieType.*;
import static org.assertj.core.api.Assertions.assertThat;

class RentalCalculatorTest {
    private RentalCalculator rentalCalculator;

    @BeforeEach
    void setUp() {
        rentalCalculator = new RentalCalculator();
    }

    @Test
    void addRental_for_childrens_LTE_3() {
        addRentalAndAssertPointsAndCharge(CHILDRENS, "childrensMovieTitle", 3, 1, 1.5);
    }

    private void addRentalAndAssertPointsAndCharge(MovieType movieType, String title, int daysRented, int expectedPoints, double expectedCharge) {
        rentalCalculator.addRental(new Movie(movieType, title), daysRented);
        assertThat(rentalCalculator.getPoints()).isEqualTo(expectedPoints);
        assertThat(rentalCalculator.getCharge()).isEqualTo(expectedCharge);
    }

    @ParameterizedTest
    @MethodSource("provideMovies")
    public void testAddRental(MovieType type, String title, int daysRented, int expectedPoints, double expectedCharge) {
        addRentalAndAssertPointsAndCharge(type, title, daysRented, expectedPoints, expectedCharge);
    }

    private static Stream<Arguments> provideMovies() {
        return Stream.of(
                Arguments.of(CHILDRENS, childrensMovieTitle, 3, 1, 1.5)
                , Arguments.of(CHILDRENS, childrensMovieTitle, 4, 1, 3.0)
                , Arguments.of(REGULAR, regularMovieTitle, 2, 1, 2.0)
                , Arguments.of(REGULAR, regularMovieTitle, 3, 1, 3.5)
                , Arguments.of(NEW_RELEASE, newRelaseMovieTitle, 1, 1, 3.0)
                , Arguments.of(NEW_RELEASE, newRelaseMovieTitle, 2, 2, 6.0)
        );
    }
}