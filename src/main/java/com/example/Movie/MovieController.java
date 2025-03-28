package com.example.Movie;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.naming.ServiceUnavailableException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final WebClient reviewClient;
    private final WebClient genreClient;
    private final MovieRepository movieRepository;

    public MovieController(WebClient.Builder reviewClientBuilder, WebClient.Builder genreClientBuilder, MovieRepository movieRepository) {
        this.reviewClient = reviewClientBuilder.baseUrl(System.getenv("REVIEW_CLIENT_URL")).build();
        this.genreClient = genreClientBuilder.baseUrl(System.getenv("GENRE_CLIENT_URL")).build();
        this.movieRepository = movieRepository;
    }

    //Create
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody @Valid Movie movie) throws BadRequestException {
        boolean bool = Boolean.TRUE.equals(genreExists(movie.getGenreId()).block());
        System.out.println(bool);
        if (bool) {
            return ResponseEntity.ok(movieRepository.save(movie));
        } else throw new BadRequestException("INVALID GENRE ID");
    }

    //Read
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            Mono<Genre> genre = getGenre(movie.getGenreId());
            Flux<Review> review = getReview(id);
            return ResponseEntity.ok(new MovieResponse(movie, genre.block(), review.collectList().block()));
        } else throw new EntityNotFoundException("INVALID MOVIE ID");

    }

    @GetMapping("/genre/{genreId}")
    public List<Movie> getMoviesByGenreId(@PathVariable int genreId) {
        return movieRepository.findByGenreId(genreId);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<MovieResponse> getMovieAndReviewsByMovieId(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        Flux<Review> review = getReview(id);
        return ResponseEntity.ok(new MovieResponse(movie, review.collectList().block()));
    }

    @GetMapping("/findByTitle/{query}")
    public ResponseEntity<List<Movie>> getMovieByQueryInTitle(@PathVariable String query) {
        List<Movie> movies = movieRepository.findAllByTitleContainingIgnoreCase(query);
        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else return ResponseEntity.notFound().build();
    }

    public Mono<Genre> getGenre(int genreId) {
        return genreClient.get()
                .uri("/genre/" + genreId)
                .retrieve()
                .bodyToMono(Genre.class)
                .retryWhen(Retry.backoff(2, Duration.of(1, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ServiceUnavailableException("UNABLE TO CONNECT TO GENRE SERVICE")
                        )
                );
    }

    public Flux<Review> getReview(Long id) {
        return reviewClient.get()
                .uri("/reviews/" + id + "/review")
                .retrieve()
                .bodyToFlux(Review.class)
                .retryWhen(Retry.backoff(2, Duration.of(1, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ServiceUnavailableException("UNABLE TO CONNECT TO REVIEW SERVICE")
                        )
                );
    }

    public Mono<Boolean> genreExists(int id) {
        return genreClient.get()
                .uri("/genre/exists/" + id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(2, Duration.of(1, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ServiceUnavailableException("UNABLE TO CONNECT TO GENRE SERVICE")
                        )
                );
    }

}
