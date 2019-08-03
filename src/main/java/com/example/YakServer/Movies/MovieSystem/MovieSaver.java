package com.example.YakServer.Movies.MovieSystem;

import com.example.YakServer.Models.Movie;

public interface MovieSaver {
    boolean validateMovie(Movie movie);

    boolean validateContent(Movie movie);

    void saveMovie(Movie movie);
}
