package com.example.presenting_tdd;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

enum MovieType {
    CHILDRENS,
    REGULAR,
    NEW_RELEASE
}

@Getter
@Entity
public class Movie {
    private MovieType type;
    private String title;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Movie(MovieType type, String title) {
        this.type = type;
        this.title = title;
    }

    public Movie() {
    }
}
