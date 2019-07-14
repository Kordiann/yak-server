package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MovieRepository;
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

    List<Movie> generateMoviesFromDb(int count) {
        List<Movie> resultList = new ArrayList<>();
        Iterable<Movie> movieListFromDb = movieRepository.findAll();
        LinkedList<Movie> checkedMovies = checkifPlotExists(movieListFromDb);

        Collections.shuffle(checkedMovies);

        for (int i = 0; i < count; i++) {
            resultList.add(checkedMovies.get(i));
        }

        return resultList;
    }

    List<Movie> generateMoviesFromDb() {
        Iterable<Movie> movieListFromDb = movieRepository.findAll();
        return checkifPlotExists(movieListFromDb);
    }

    List<Movie> generateLastestMoviesFromDb(int count) {
        List<Movie> movies = generateMoviesFromDb();

        List<Movie> lastestMovies = new ArrayList<>();

        for (Movie movie :
                movies) {
            if (Integer.parseInt(movie.getYear()) >= 2018) {
                lastestMovies.add(movie);
            }
        }

        List<Movie> movieResult = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            movieResult.add(lastestMovies.get(i));
        }

        return movieResult;
    }

    private LinkedList<Movie> checkifPlotExists(Iterable<Movie> moviesToCheck) {
        LinkedList<Movie> movies = Lists.newLinkedList(moviesToCheck);
        List<Movie> moviesToDelete = new ArrayList<>();

        User blankUserForFill = new User();
        List<User> blankListUsersForFill = new ArrayList<>();

        blankListUsersForFill.add(blankUserForFill);

        for (Movie movie :
                    movies) {
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
}
