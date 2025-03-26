package com.example.Movie;

import java.util.List;

public class MovieResponse {

    private Movie movie;
    private List<Genre> genre;

    public MovieResponse(Movie movie, List<Genre> genre) {
        this.movie = movie;
        this.genre = genre;
    }

    public MovieResponse() {
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }
}
