package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class MovieService {
    List<Movie> resultList = new ArrayList<>();
    Connector connector;

    LinkedList<Movie> deleteMoviesWithoutPosters(LinkedList<Movie> moviesToCheck) {
        List<Movie> moviesToRemove = new ArrayList<>();

        System.out.println(moviesToCheck);

        for (Movie movie :
                moviesToCheck) {
            String URLName = movie.getPoster();

            if(checkIfPosterExists(URLName)) moviesToRemove.add(movie);
        }

        for (Movie movieToRemove :
                    moviesToRemove) {
            moviesToCheck.remove(movieToRemove);
        }

        System.out.println(moviesToCheck);

        return moviesToCheck;
    }

    private Boolean checkIfPosterExists(String URLName) {
//        TODO Make better implementation
        try {
            return URLName.equals("N/A");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    List<Movie> getResultList() {
        return resultList;
    }
}
