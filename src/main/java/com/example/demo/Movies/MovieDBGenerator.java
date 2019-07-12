package com.example.demo.Movies;

import com.example.demo.Models.Movie;
import com.example.demo.Models.User;
import com.example.demo.Repositories.MovieRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class MovieDBGenerator {
    private MovieRepository movieRepository;

    @Autowired
    MovieDBGenerator(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    List<Movie> generateMoviesFromDb() {
        List<Movie> resultList = new ArrayList<>();
        Iterable<Movie> movieListFromDb = movieRepository.findAll();
        LinkedList<Movie> checkedMovies = checkifPlotExists(movieListFromDb);

        Collections.shuffle(checkedMovies);

        for (int i = 0; i < 8; i++) {
            System.out.println(checkedMovies.get(i));

            resultList.add(checkedMovies.get(i));
        }

        return resultList;
    }

    private LinkedList<Movie> checkifPlotExists(Iterable<Movie> moviesToCheck) {
        LinkedList<Movie> movies = Lists.newLinkedList(moviesToCheck);
        List<Movie> moviesToDelete = new ArrayList<>();

        User blankUserForFill = new User();
        List<User> blankListUsersForFill = new ArrayList<>();

        blankListUsersForFill.add(blankUserForFill);

        for (Movie movie :
                    movies) {
//            System.out.println(movie.toString());
//            System.out.println(movie.getPlot());

            if (movie.getPlot() == null) moviesToDelete.add(movie);
        }

        for (Movie movieToDelete :
                    moviesToDelete) {
            movies.remove(movieToDelete);
        }

        for (Movie movie :
                    movies) {
            movie.setSaver(blankListUsersForFill);
        }

        return movies;
    }

//    public List<Movie> getResultList() {
//        return resultList;
//    }
}
