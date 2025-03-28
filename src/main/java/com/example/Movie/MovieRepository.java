package com.example.Movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByTitleContainingIgnoreCase(String query);
    List<Movie> findByGenreId(int genreId);
    List<Movie> findAllByReleased(int released);
    List<Movie> findAllByDirectorContainingIgnoreCase(String query);

}
