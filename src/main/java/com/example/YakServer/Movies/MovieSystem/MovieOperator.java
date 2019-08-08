package com.example.YakServer.Movies.MovieSystem;

import com.example.YakServer.Models.Movie;

import com.example.YakServer.Repositories.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Optional;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MovieOperator implements MovieSaver {
    private MovieRepository movieRepo;

    private final Logger logger = Logger.getLogger(MovieOperator.class.getName());

    @Autowired
    public MovieOperator(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
    }

    @Override
    public void saveMovie(Movie movie) {
        if(validateMovie(movie)) {
            movieRepo.save(movie);
        } else {
            logger.log(Level.WARNING, "Can not add Movie : " + movie.getTitle());
        }
    }

    @Override
    public boolean validateMovie(Movie movie) {
        Optional<Movie> optionalMovie = movieRepo.findByImdbID(movie.getImdbID());

        return validateContent(movie) && !optionalMovie.isPresent();
    }

    @Override
    public boolean validateContent(Movie movie) {
        return (!movie.getPoster().equals("N/A") && !movie.getPlot().equals("N/A"));
    }
}
