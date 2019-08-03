package com.example.YakServer.Responds.Movies;

import com.example.YakServer.Responds.SimplifiedModels.SMovie;

import java.util.List;

public class MoviesResponse {
    private String Response;
    private List<SMovie> SMovies;

    public MoviesResponse() {}

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<SMovie> getSMovies() {
        return SMovies;
    }

    public void setSMovies(List<SMovie> SMovies) {
        this.SMovies = SMovies;
    }
}
