package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Models.User;

import com.example.YakServer.Movies.MovieSystem.APIGenerator;
import com.example.YakServer.Movies.MovieSystem.Connector;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;

import com.example.YakServer.Responds.Movies.SavedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
class MovieService {
    private MovieRepository movieRepo;
    private UserRepository userRepo;

    private SavedResponse savedRes;

    private final Connector connector;

    private final Logger logger = Logger.getLogger(MovieService.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public MovieService(MovieRepository movieRepo, UserRepository userRepo) {
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.connector = new Connector();
    }

    public String execute(String imdbID, User user) {
        try {
            Optional<Movie> movieOptional = movieRepo.findByImdbID(imdbID);

            savedRes = new SavedResponse();

            if (movieOptional.isPresent()) {
                make(movieOptional.get(), user);

                savedRes.setResponse("200");
                savedRes.setUserName(user.getUserName());
                savedRes.setMovieTitle(movieOptional.get().getTitle());

            } else {
                Optional<Movie> optionalGeneratedMovie = connector.getMovieById(imdbID);

                if (optionalGeneratedMovie.isPresent()) {
                    make(optionalGeneratedMovie.get(), user);

                    savedRes.setResponse("200");
                    savedRes.setUserName(user.getUserName());
                    savedRes.setMovieTitle(optionalGeneratedMovie.get().getTitle());

                } else {
                    savedRes.setResponse("400");
                    logger.log(Level.WARNING, "Can not assign user to movie");
                }
            }

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedRes);
        } catch(JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    private void make(Movie movie, User user) {
        movie.getSaver().add(user);
        movieRepo.save(movie);

        user.getSavedMovies().add(movie);
        userRepo.save(user);
    }

    String error() {
        try {
            savedRes = new SavedResponse();

            savedRes.setResponse("400");

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(savedRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }
}
