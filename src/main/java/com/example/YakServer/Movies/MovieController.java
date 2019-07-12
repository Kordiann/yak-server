package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/movie")
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

    @GetMapping(path="/main")
    public @ResponseBody
    List<Movie> getMainPageMovies() {
        MovieDBGenerator movieDBGenerator = new MovieDBGenerator(movieRepository);

        for (Movie movie :
                movieDBGenerator.generateMoviesFromDb()) {
            System.out.println(movie.getSaver());
        }

        return movieDBGenerator.generateMoviesFromDb();
    }

    @GetMapping()
    public @ResponseBody
    Iterable<Movie> getAllMovies(@RequestParam String title) {
        MovieSearch movieSearch = new MovieSearch("s", title);

        return movieSearch.getResultList();
    }

//    ---------- DELETE METHODS ----------      //

    @DeleteMapping(path = "/delete_movie")
    public @ResponseBody
    String deleteMovieFromUser(@RequestParam String movieIMDBID, @RequestParam String userName) {
        MovieOperator movieOperator = new MovieOperator(userRepository, movieRepository);

        return  movieOperator.deleteMovieFromUser(movieIMDBID, userName);
    }
}
