package com.example.demo.Movies;

import com.example.demo.Models.Movie;
import com.example.demo.Models.User;
import com.example.demo.Repositories.MovieRepository;
import com.example.demo.Repositories.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieOperator {

    private UserRepository userRepository;
    private MovieRepository movieRepository;

    @Autowired
    MovieOperator(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;

    }

    String assignMovieToUser(String movieIMDBID, String userName) {
        String response = "";
        boolean hasAlreadySaved = false;

        if (isUserExisting(userName)) {
                User user = userRepository.findByUserName(userName);
                Connector connector = new Connector("i", movieIMDBID);
                boolean movieExists = false;

                Iterable<Movie> movieIterable = movieRepository.findAll();
                List<Movie> movieList = Lists.newArrayList(movieIterable);

                for (Movie movieInDb :
                        movieList) {
                    if (movieInDb.getImdbID().equals(movieIMDBID)) {
                        movieExists = true;

                        List<Movie> alreadySavedMovies = user.getSavedMovies();

                        for (Movie alreadySavedMovie :
                                alreadySavedMovies) {
                            if (alreadySavedMovie.getImdbID().equals(movieIMDBID)) {
                                response = "movie has been already saved";
                                hasAlreadySaved = true;
                            }
                        }

                        if (!hasAlreadySaved) {

                            movieInDb.getSaver().add(user);
                            user.getSavedMovies().add(movieInDb);

                            movieRepository.save(movieInDb);
                            userRepository.save(user);
                            response = "successful saved movie";
                        }
                    }
                }

                if (!movieExists) {
                    Movie movie = connector.getMovies().getFirst();
                    user.getSavedMovies().add(movie);

                    movieRepository.save(movie);
                    userRepository.save(user);

                    response = "successful saved movie";
                }

            } else {
                response = "cannot find user with this id";
            }

        return response;
    }

    String deleteMovieFromUser(String movieIMDBID, String userName) {
        String response = "";

        if (isUserExisting(userName)) {
            User user = userRepository.findByUserName(userName);

            Iterable<Movie> movieIterable = movieRepository.findAll();
            List<Movie> movieList = Lists.newArrayList(movieIterable);

            for (Movie movieinDb :
                        movieList) {
                if(movieinDb.getImdbID().equals(movieIMDBID)) {
                    user.getSavedMovies().remove(movieinDb);
                    userRepository.save(user);
                    movieinDb.getSaver().remove(user);
                    movieRepository.save(movieinDb);
                    response = "successful deleted movie " + movieIMDBID + " from user " + userName;
                }
            }


        } else {
            response = "cannot find user with this id or that movie doesnt exists";
        }

        return response;
    }

    private boolean isUserExisting(String userName) {
        Iterable<User> users = userRepository.findAll();
        boolean existing = false;
        int userID = -1;

        for (User u :
                users) {
            if (u.getUserName().equals(userName)) userID = u.getId();
        }

        if(userID != -1) existing = true;

        return existing;
    }
}
