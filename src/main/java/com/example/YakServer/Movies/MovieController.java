package com.example.YakServer.Movies;


import com.example.YakServer.Models.User;
import com.example.YakServer.Movies.MovieSystem.DBGenerator;

import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "https://yak-client.herokuapp.com")
@RequestMapping(path = "/movies")
public class MovieController {
    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private UserRepository userRepo;

    //    ---------- GET METHODS ----------      //

    @GetMapping(path = "/home")
    public @ResponseBody
    String getHomeMovies () {
        DBGenerator db = new DBGenerator(movieRepo);
        return db.execute("home", 4);
    }

    @GetMapping(path = "/search")
    public @ResponseBody
    String getSearchMovies () {
        DBGenerator db = new DBGenerator(movieRepo);
        return db.execute("search", 20);
    }

    @GetMapping(path = "/")
    public @ResponseBody
    String searchMovie (@RequestParam String condition, @RequestParam String phrase,
                 @RequestParam(value = "userID", required=false) Integer userID ) {
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

    @PostMapping(path = "/save/movie")
    public @ResponseBody
    String saveMovie (@RequestParam String imdbID, @RequestParam Integer userID) {
        Optional<User> optionalUser = userRepo.findById(userID);
        MovieService ms = new MovieService(movieRepo, userRepo);

        if(optionalUser.isPresent()) {
            return ms.execute(imdbID, optionalUser.get());
        } else {
            return ms.error();
        }
    }
}
