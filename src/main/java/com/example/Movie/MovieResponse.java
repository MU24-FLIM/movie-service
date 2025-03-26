package com.example.Movie;

import java.util.List;

public class MovieResponse {

    private Movie movie;
    private Genre genre;
    private List<Review> reviews;

    public MovieResponse(Movie movie, Genre genre, List<Review> reviews) {
        this.movie = movie;
        this.genre = genre;
        this.reviews = reviews;
    }

    public MovieResponse(Movie movie, List<Review> reviews) {
        this.movie = movie;
        this.reviews = reviews;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
