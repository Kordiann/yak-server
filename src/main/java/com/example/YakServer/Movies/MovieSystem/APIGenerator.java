package com.example.YakServer.Movies.MovieSystem;

import com.example.YakServer.Models.Movie;

import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MovieRepository;

import com.example.YakServer.Responds.Movies.MoviesResponse;
import com.example.YakServer.Responds.SimplifiedModels.SMovie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.cookie.SM;
import org.springframework.beans.factory.annotation.Autowired
        ;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class APIGenerator {
    private final Connector connector;
    private final MovieOperator movieOperator;

    private final Logger logger = Logger.getLogger(APIGenerator.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public APIGenerator(MovieRepository movieRepo) {
        this.connector = new Connector();
        this.movieOperator = new MovieOperator(movieRepo);
    }

    String generateMovie(String imdbID) {
        Optional<Movie> movieOptional = connector.getMovieById(imdbID);

        if (movieOptional.isPresent()) {
            movieOperator.saveMovie(movieOptional.get());

            return generateResponse(movieOptional.get());
        } else {
            return generateResponse();
        }
    }

    String generateMovies(String title) {
        Optional<List<Movie>> moviesOptional = connector.getMoviesByTitle(title);

        if (moviesOptional.isPresent()) {
            List<Movie> movies = moviesOptional.get();

            pushMoviesToSave(movies);

            return generateResponse(movies);
        } else {
            return generateResponse();
        }
    }

    private void pushMoviesToSave(List<Movie> movies) {
        for(Movie movie :
                    movies) {
            Optional<Movie> optionalMovie = connector.getMovieById(movie.getImdbID());

            if(optionalMovie.isPresent()) {
                movieOperator.saveMovie(optionalMovie.get());
            } else  {
                logger.log(Level.INFO, movie.getTitle() + " This Movie perhaps does not exists");
            }
        }
    }

    private String generateResponse() {
        try {
            MoviesResponse movieRes = new MoviesResponse();
            movieRes.setResponse("400");

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    String generateResponse(Movie movie) {
        try {
            MoviesResponse movieRes = new MoviesResponse();

            List<SMovie> sMovies = new ArrayList<>();
            sMovies.add(parseMovieToSMovie(movie));

            movieRes.setResponse("200");
            movieRes.setSMovies(sMovies);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    String generateResponse(Movie movie, List<Movie> userMovies) {
        try {
            MoviesResponse movieRes = new MoviesResponse();

            List<SMovie> sMovies = new ArrayList<>();
            sMovies.add(parseMovieToSMovie(movie));

            movieRes.setResponse("200");
            movieRes.setSMovies(validateUserMovies(sMovies, userMovies));

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    String generateResponse(List<Movie> movies) {
        try {
            MoviesResponse movieRes = new MoviesResponse();

            List<SMovie> sMovies = new ArrayList<>();

            for(Movie movie :
                    movies) {
                sMovies.add(parseMovieToSMovie(movie));
            }

            movieRes.setResponse("200");
            movieRes.setSMovies(sMovies);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    String generateResponse(List<Movie> movies, List<Movie> userMovies) {
        try {
            MoviesResponse movieRes = new MoviesResponse();

            List<SMovie> sMovies = new ArrayList<>();

            for(Movie movie :
                    movies) {
                sMovies.add(parseMovieToSMovie(movie));
            }

            movieRes.setResponse("200");
            movieRes.setSMovies(validateUserMovies(sMovies, userMovies));

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(movieRes);
        } catch (JsonProcessingException js) {
            throw new RuntimeException(js.getMessage());
        }
    }

    private SMovie parseMovieToSMovie(Movie movie) {
        SMovie smovie = new SMovie();

        smovie.setImdbID(movie.getImdbID());
        smovie.setPlot(movie.getPlot());
        smovie.setPoster(movie.getPoster());
        smovie.setTitle(movie.getTitle());
        smovie.setType(movie.getType());
        smovie.setYear(movie.getYear());

        return smovie;
    }

    private List<SMovie> validateUserMovies(List<SMovie> sMovies, List<Movie> userMovies) {
        for(SMovie sMovie :
                sMovies) {
            for(Movie userMovie :
                    userMovies) {
                if(sMovie.getImdbID().equals(userMovie.getImdbID())) {
                    sMovie.setSaved(true);
                    break;
                }
            }
        }

        return sMovies;
    }
}
