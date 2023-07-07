package com.example.presenting_tdd;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RentalController {
    private final MovieRepository movieRepository;

    public RentalController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostMapping("/rentals")
    public RentalResponse addRental(@RequestBody RentalRequest rentalRequest) {
        Movie movie = movieRepository.findByTitle(rentalRequest.title());
        RentalCalculator rentalCalculator = getRentalCalculator();
        rentalCalculator.addRental(movie, rentalRequest.daysRented());
        return new RentalResponse(rentalCalculator.getPoints(), rentalCalculator.getCharge());
    }

    // subclass and override
    RentalCalculator getRentalCalculator() {
        return new RentalCalculator();
    }
}

record RentalRequest(String title, int daysRented) {
}

record RentalResponse(int frequentRentalPoints, double charge) {
}
