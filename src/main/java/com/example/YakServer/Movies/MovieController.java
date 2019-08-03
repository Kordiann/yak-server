package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;

import com.example.YakServer.Models.User;
import com.example.YakServer.Movies.MovieSystem.DBGenerator;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/movies")
public class MovieController {
    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private UserRepository userRepo;

    //    ---------- GET METHODS ----------      //

    @GetMapping(path = "/test")
    public @ResponseBody
    String test (@RequestParam String condition, @RequestParam String phrase,
                 @RequestParam(value = "i", required=false) Integer userID ) {
        DBGenerator db = new DBGenerator(movieRepo);

        if (userID != null) {
            Optional<User> user = userRepo.findById(userID);

            if(user.isPresent()) {
                return db.execute(condition, phrase, user.get());
            } else {
                return db.execute(condition, phrase);
            }
        } else {
            return db.execute(condition, phrase);
        }
    }


//    ---------- POST METHODS ----------      //

//    @PostMapping(path = "/save/movie")
//    public @ResponseBody
//    String saveMovie (@RequestParam String movieIMDBID, @RequestParam String userName) {
//        MovieOperator movieOperator = new MovieOperator(userRepo, movieRepo);
//
//        return movieOperator.assignMovieToUser(movieIMDBID, userName);
//    }

//    ---------- GET METHODS ----------      //

    private void saveMovieToDb(Movie movieToCheckIfExists) {
        boolean toSave = true;
        List<Movie> moviesFromDb = movieRepo.findAll();

        for(Movie movieFromDb :
                moviesFromDb) {
            if (movieFromDb.getImdbID().equals(movieToCheckIfExists.getImdbID())) {
                toSave = false;
                break;
            }
        }

        if(toSave) movieRepo.save(movieToCheckIfExists);
    }
}
