package com.example.YakServer.Responds;

import com.example.YakServer.Models.Movie;

import java.util.List;

public class MoviesResponse {
    private String Response;
    private List<Movie> Movies;

    public MoviesResponse() {}

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<Movie> getMovies() {
        return Movies;
    }

    public void setMovies(List<Movie> movies) {
        Movies = movies;
    }
}
