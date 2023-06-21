package com.example.presenting_tdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddRentalsAcceptanceTest {
    @Autowired
    private MovieRepository movieRepository;
    private String childrensMovieTitle = "childrensMovieTitle";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void walking_skeleton() throws JsonProcessingException {
        movieRepository.save(new Movie(childrensMovieTitle.trim()));

        final Movie rentalResponse =
                given()
                .when()
                        .get("/rentals?title=childrensMovieTitle")
                .then()
                        .statusCode(200)
                        .extract()
                        .as(Movie.class);

        System.out.println(rentalResponse);
    }
}