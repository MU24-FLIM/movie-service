package com.example.Movie;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody @Valid Movie movie) throws BadRequestException {
//        boolean bool = Boolean.TRUE.equals(genreExists(movie.getGenreId()).block());
//        System.out.println(bool);
//        if (bool) {
            return ResponseEntity.ok(movieRepository.save(movie));
//        } else throw new BadRequestException("INVALID GENRE ID");
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        Mono<Genre> genre = getGenre(id);
        Flux<Review> review = getReview(id);
        return ResponseEntity.ok(new MovieResponse(movie, genre.block(), review.collectList().block()));
    }

    @GetMapping("/genre/{genreId}")
    public List<Movie> getMoviesById(@PathVariable int genreId){
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

    public Mono<Genre> getGenre(Long id) {
        return movieRepository.findById(id).map(movie ->
                        genreClient.get()
                                .uri("/genre/" + movie.getGenreId())
                                .retrieve()
                                .bodyToMono(Genre.class))
                .orElse(null);
    }

    public Flux<Review> getReview(Long id) {
        return movieRepository.findById(id).map(movie ->
                        reviewClient.get()
                                .uri("/reviews/" + movie.getId())
                                .retrieve()
                                .bodyToFlux(Review.class))
                .orElse(Flux.empty());
    }


    public Mono<Boolean> genreExists(int id) {
        return genreClient.get()
                .uri("/genre/exists/" + id)
                .retrieve()
                .onStatus(HttpStatusCode.valueOf(404)::equals, res -> Mono.error(new EntityNotFoundException("Book ID not found (response from book) (from loan)")))
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> Mono.error(new BadRequestException("Book is not available (response from book)(from loan) ")))
                .bodyToMono(Boolean.class);
    }

}
