package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

//    ---------- POST METHODS ----------      //

    @PostMapping(path = "/save/movie")
    public @ResponseBody
    String saveMovie (@RequestParam String movieIMDBID, @RequestParam String userName) {
        MovieOperator movieOperator = new MovieOperator(userRepository, movieRepository);

        return movieOperator.assignMovieToUser(movieIMDBID, userName);
    }

//    ---------- GET METHODS ----------      //

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Movie> getAllMovies(@RequestParam Integer count) {
        MovieAPIGenerator movieAPIGenerator = new MovieAPIGenerator(count);
        return movieAPIGenerator.getResultList();
    }

    @GetMapping(path="/home")
    public @ResponseBody
    List<Movie> getHomePageMovies(@RequestParam Integer count) {
        MovieDBGenerator movieDBGenerator = new MovieDBGenerator(movieRepository);
        return movieDBGenerator.generateLastestMoviesFromDb(count);
    }

    @GetMapping(path="/search")
    public @ResponseBody
    List<Movie> getSearchPageMovies(@RequestParam Integer count) {
        MovieDBGenerator movieDBGenerator = new MovieDBGenerator(movieRepository);
        return movieDBGenerator.generateMoviesFromDb(count);
    }

    @GetMapping()
    public @ResponseBody
    Iterable<Movie> getAllMovies(@RequestParam String title) {
        MovieSearch movieSearch = new MovieSearch("s", title);
        return movieSearch.getResultList();
    }

//    TODO SEARCH ONE MOVIE

    @GetMapping("/movie")
    public @ResponseBody
    Movie getOneMovie(@RequestParam String id) {
        MovieSearch movieSearch = new MovieSearch("i", id);

        saveMovieToDb(movieSearch.getResultMovie());

        return movieSearch.getResultMovie();
    }

//    ---------- DELETE METHODS ----------      //

    @DeleteMapping(path = "/delete_movie")
    public @ResponseBody
    String deleteMovieFromUser(@RequestParam String movieIMDBID, @RequestParam String userName) {
        MovieOperator movieOperator = new MovieOperator(userRepository, movieRepository);

        return  movieOperator.deleteMovieFromUser(movieIMDBID, userName);
    }

//    TODO Try new Feature

    private void saveMovieToDb(List<Movie> moviesToCheckIfExists) {
        boolean toSave = true;

        List<Movie> moviesFromDb = movieRepository.findAll();
        List<Movie> moviesToSave = new ArrayList<>();

        for(Movie movieToCheck :
                    moviesToCheckIfExists) {
            for(Movie movieFromDb :
                        moviesFromDb) {
                if (movieToCheck == movieFromDb) {
                    toSave = false;
                    break;
                }
            }
            if(toSave) moviesToSave.add(movieToCheck);
        }

        for(Movie movieToSave :
                moviesToSave) {
            movieRepository.save(movieToSave);
        }
    }

    private void saveMovieToDb(Movie movieToCheckIfExists) {
        boolean toSave = true;
        List<Movie> moviesFromDb = movieRepository.findAll();

        for(Movie movieFromDb :
                moviesFromDb) {
            if (movieFromDb.getImdbID().equals(movieToCheckIfExists.getImdbID())) {
                toSave = false;
                break;
            }
        }

        if(toSave) movieRepository.save(movieToCheckIfExists);
    }
}
