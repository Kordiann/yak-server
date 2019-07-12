package com.example.YakServer.Movies;

import com.example.YakServer.Models.Movie;

import java.util.LinkedList;

class MovieSearch extends MovieService {

    MovieSearch(String type_search, String phrase) {
        this.connector = new Connector(type_search, phrase);

        if(type_search.equals("s")) {
            generateResults();
        } else if (type_search.equals("i")) {
            generateResult();
        }
    }

    private void generateResults() {
        LinkedList<Movie> moviesToCheck = connector.getMovies();

        resultList = deleteMoviesWithoutPosters(moviesToCheck);
    }

    private void generateResult() {
        LinkedList<Movie> resultMovies = connector.getMovies();

        resultMovie = resultMovies.get(0);
    }
}
