package com.example.YakServer.Repositories;

import com.example.YakServer.Models.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findAll();

    Optional<Movie> findByImdbID(String imdbID);
    Optional<Movie> findById(Integer id);
    Optional<List<Movie>> findAllByTitleContaining(String title);

    Movie save(Movie movie);
}
