package com.example.Movie;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    //
//    private WebClient reviewClient;
//    private WebClient genreClient;
    private final MovieRepository movieRepository;

//
//    public MovieController(WebClient.Builder reviewClient, WebClient.Builder genreClient, MovieRepository movieRepository) {
//        this.reviewClient = reviewClient.baseUrl().build();
//        this.genreClient = genreClient.baseUrl().build();
//        this.movieRepository = movieRepository;
//    }

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody @Valid Movie movie) {
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else return ResponseEntity.notFound().build();
    }


    @GetMapping("/findByTitle/{query}")
    public ResponseEntity<List<Movie>> getMovieByQueryInTitle(@PathVariable String query) {
        List<Movie> movies = movieRepository.findAllByTitleContainingIgnoreCase(query);
        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else return ResponseEntity.notFound().build();
    }


}
