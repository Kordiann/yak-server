package com.example.demo.Movies;

import com.example.demo.Models.Movie;

import java.util.LinkedList;

class MovieSearch extends MovieService {
    MovieSearch(String type_search, String title) {
        this.connector = new Connector(type_search, title);

        generateResult();
    }

    private void generateResult() {
        LinkedList<Movie> moviesToCheck = connector.getMovies();

        resultList = deleteMoviesWithoutPosters(moviesToCheck);
    }
}
