package com.example.presenting_tdd;

public class RentalCalculator {
    private Movie movie;
    private Integer daysRented;

    public void addRental(Movie movie, Integer daysRented) {
        this.movie = movie;
        this.daysRented = daysRented;
    }

    public Integer getPoints() {
        if(movie.getType() == MovieType.NEW_RELEASE && daysRented > 1)
            return 2;
        else
            return 1;
    }

    public Double getCharge() {
        double charge = 0;
        if(movie.getType() == MovieType.CHILDRENS) {
            charge = 1.5;
            if (daysRented > 3) {
                charge += (daysRented - 3) * 1.5;
            }
        }
        else if (movie.getType() == MovieType.REGULAR) {
            charge = 2.0;
            if(daysRented > 2) {
                charge += (daysRented - 2) * 1.5;
            }
        }
        else if(movie.getType() == MovieType.NEW_RELEASE) {
            charge = daysRented * 3.0;
        }
        return charge;
    }
}