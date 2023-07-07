package com.example.presenting_tdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddRentalsAcceptanceTest {
    @Autowired
    private MovieRepository movieRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    static final String childrensMovieTitle = "childrensMovieTitle";
    static final String regularMovieTitle = "regular";
    static final String newRelaseMovieTitle = "newRelase";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        movieRepository.save(new Movie(MovieType.CHILDRENS, childrensMovieTitle));
        movieRepository.save(new Movie(MovieType.REGULAR, regularMovieTitle));
        movieRepository.save(new Movie(MovieType.NEW_RELEASE, newRelaseMovieTitle));
    }

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("provideMovies")
    public void testAddRental(String title, int daysRented, int expectedPoints, double expectedCharge) throws JsonProcessingException {
        final RentalRequest request = new RentalRequest(title, daysRented);

        final RentalResponse rentalResponse =
                given()
                        .contentType(ContentType.JSON)
                        .body(objectMapper.writeValueAsString(request))
                        .when()
                        .post("/rentals")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(RentalResponse.class);

        assertThat(rentalResponse.frequentRentalPoints()).isEqualTo(expectedPoints);
        assertThat(rentalResponse.charge()).isEqualTo(expectedCharge);
    }

    private static Stream<Arguments> provideMovies() {
        return Stream.of(
                Arguments.of(childrensMovieTitle, 3, 1, 1.5)
//                , Arguments.of(childrensMovieTitle, 4, 1, 3.0)
//                , Arguments.of(regularMovieTitle, 2, 1, 2.0)
//                , Arguments.of(regularMovieTitle, 3, 1, 3.5)
//                , Arguments.of(newRelaseMovieTitle, 1, 1, 3.0)
//                , Arguments.of(newRelaseMovieTitle, 2, 2, 6.0)
        );
    }
}