package com.example.Movie;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    //Create
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    //Read
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> getMoviesByGenreId(int genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    public List<Movie> getMovieByQueryInTitle(String query) {
        List<Movie> movies = movieRepository.findAllByTitleContainingIgnoreCase(query);
        if (!movies.isEmpty()) {
            return movies;
        } else throw new EntityNotFoundException("NO MATCH");
    }

    public List<Movie> getMovieByQueryInDirector(String query) {
        List<Movie> movies = movieRepository.findAllByDirectorContainingIgnoreCase(query);
        if (!movies.isEmpty()) {
            return movies;
        } else throw new EntityNotFoundException("NO MATCH");
    }

    public List<Movie> getMovieByReleased(int query) throws BadRequestException {
        if (query > 1900 && query < 2050) {
            List<Movie> movies = movieRepository.findAllByReleased(query);
            if (!movies.isEmpty()) {
                return movies;
            } else throw new EntityNotFoundException("NO MATCH");
        } else throw new BadRequestException("INVALID YEAR");
    }

    //UPDATE
    public Movie updateMovieById(Long id, Movie updatedMovie) {
        Movie movieToUpdate = getMovieById(id);
        if (movieToUpdate != null) {
            movieToUpdate = updatedMovie;
            return addMovie(movieToUpdate);
        } else throw new EntityNotFoundException("INVALID MOVIE ID");
    }

    //DELETE
    public String deleteMovieById(Long id) {
        if (isValidMovieId(id)) {
            movieRepository.deleteById(id);
            return "DELETED SUCCESSFULLY";
        } else throw new EntityNotFoundException("INVALID MOVIE ID");
    }

    private boolean isValidMovieId(Long id) {
        return movieRepository.existsById(id);
    }

}
