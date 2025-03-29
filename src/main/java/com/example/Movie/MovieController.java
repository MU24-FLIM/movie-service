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
    private final MovieService movieService;

    public MovieController(WebClient.Builder reviewClientBuilder, WebClient.Builder genreClientBuilder, MovieService movieService) {
        this.reviewClient = reviewClientBuilder.baseUrl(System.getenv("REVIEW_CLIENT_URL")).build();
        this.genreClient = genreClientBuilder.baseUrl(System.getenv("GENRE_CLIENT_URL")).build();
        this.movieService = movieService;
    }

    //Create
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody @Valid Movie movie) throws BadRequestException {
        if (genreExists(movie.getGenreId())) {
            return ResponseEntity.ok(movieService.addMovie(movie));
        } else throw new BadRequestException("INVALID GENRE ID");
    }

    //Read
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        if (movie != null) {
            Mono<Genre> genre = getGenre(movie.getGenreId());
            Flux<Review> review = getReview(id);
            return ResponseEntity.ok(new MovieResponse(movie, genre.block(), review.collectList().block()));
        } else throw new EntityNotFoundException("INVALID MOVIE ID");
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<Movie>> getMoviesByGenreId(@PathVariable int genreId) {
        return ResponseEntity.ok(movieService.getMoviesByGenreId(genreId));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<MovieResponse> getMovieAndReviewsByMovieId(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        Flux<Review> review = getReview(id);
        return ResponseEntity.ok(new MovieResponse(movie, review.collectList().block()));
    }

    @GetMapping("/find/byTitle/{query}")
    public ResponseEntity<List<Movie>> getMovieByQueryInTitle(@PathVariable String query) {
        return ResponseEntity.ok(movieService.getMovieByQueryInTitle(query));
    }

    @GetMapping("/find/byDirector/{query}")
    public ResponseEntity<List<Movie>> getMovieByQueryInDirector(@PathVariable String query) {
        return ResponseEntity.ok(movieService.getMovieByQueryInDirector(query));
    }

    @GetMapping("/find/byReleaseYear/{query}")
    public ResponseEntity<List<Movie>> getMovieByReleased(@PathVariable int query) throws BadRequestException {
        return ResponseEntity.ok(movieService.getMovieByReleased(query));
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovieById(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        return ResponseEntity.ok(movieService.updateMovieById(id, updatedMovie));
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.deleteMovieById(id));
    }

    //Helper methods
    private Mono<Genre> getGenre(int genreId) {
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

    private Flux<Review> getReview(Long id) {
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

    private boolean genreExists(int id) {
        return Boolean.TRUE.equals(genreClient.get()
                .uri("/genre/exists/" + id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(2, Duration.of(1, ChronoUnit.SECONDS))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ServiceUnavailableException("UNABLE TO CONNECT TO GENRE SERVICE")
                        )
                ).block());
    }

}
