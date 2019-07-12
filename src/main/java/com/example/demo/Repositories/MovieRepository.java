package com.example.demo.Repositories;

import com.example.demo.Models.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Integer> {
    List<Movie> findAll();
    Movie save(Movie movie);
}
