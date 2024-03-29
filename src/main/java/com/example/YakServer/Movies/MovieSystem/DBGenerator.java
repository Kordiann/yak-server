package com.example.YakServer.Movies.MovieSystem;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MovieRepository;

import com.example.YakServer.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DBGenerator {
    private MovieRepository movieRepo;

    private APIGenerator APIGenerator;

    private final Logger logger = Logger.getLogger(DBGenerator.class.getName());

    @Autowired
    public DBGenerator(MovieRepository movieRepo) {
        this.movieRepo = movieRepo;
        this.APIGenerator = new APIGenerator(movieRepo);
    }

    public String execute(String value, Integer count) {
        String result = null;

        switch(value) {
            case "home" : {
                result = generateHome(count);
                break;
            }

            case "search" : {
                result = generateSearch(count);
                break;
            }

            default: {
                logger.log(Level.WARNING, "Search Condition Incorrect");
            }
        }

        return result;
    }

    public String execute(String value, String phrase, User user) {
        String result = null;

        switch(value) {
            case "s" : {
                result = generateMovies(phrase, user);
                break;
            }

            case "i" : {
                result = generateMovie(phrase, user);
                break;
            }

            default: {
                logger.log(Level.WARNING, "Search Condition Incorrect");
            }
        }

        return result;
    }

    public String execute(String value, String phrase) {
        String result = null;

        switch(value) {
            case "s" : {
                result = generateMovies(phrase);
                break;
            }

            case "i" : {
                result = generateMovie(phrase);
                break;
            }

            default: {
                logger.log(Level.WARNING, "Search Condition Incorrect");
            }
        }

        return result;
    }

    private String generateMovie(String imdbID, User user) {
        Optional<Movie> optionalMovie = movieRepo.findByImdbID(imdbID);

        if(optionalMovie.isPresent()) {
            return APIGenerator.generateResponse(optionalMovie.get(), user.getSavedMovies());
        } else {
            return APIGenerator.generateMovie(imdbID);
        }
    }

    private String generateMovies(String title, User user) {
        Optional<List<Movie>> optionalMovies = movieRepo.findAllByTitleContaining(title);

        if(optionalMovies.isPresent()) {
            return APIGenerator.generateResponse(optionalMovies.get(), user.getSavedMovies());
        } else {
            return APIGenerator.generateMovies(title);
        }
    }

    private String generateMovie(String imdbID) {
        Optional<Movie> optionalMovie = movieRepo.findByImdbID(imdbID);

        if(optionalMovie.isPresent()) {
            return APIGenerator.generateResponse(optionalMovie.get());
        } else {
            return APIGenerator.generateMovie(imdbID);
        }
    }

    private String generateMovies(String title) {
        Optional<List<Movie>> optionalMovies = movieRepo.findAllByTitleContaining(title);

        if(optionalMovies.isPresent()) {
            return APIGenerator.generateResponse(optionalMovies.get());
        } else {
            return APIGenerator.generateMovies(title);
        }
    }

    private String generateHome(Integer count) {
        List<Movie> movies = movieRepo.findAll();

        movies.removeIf(movie -> movie.getYear() < 2019);

        return APIGenerator.generateResponse(sliceList(movies, count));
    }

    private String generateSearch(Integer count) {
        List<Movie> movies = movieRepo.findAll();

        Collections.shuffle(movies);

        return APIGenerator.generateResponse(sliceList(movies, count));
    }

    private List<Movie> sliceList(List<Movie> movies, Integer count) {
        while(movies.size() > count) {
            movies.remove(movies.size() - 1);
        }

        return movies;
    }
}
