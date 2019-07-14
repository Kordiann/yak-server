package com.example.YakServer.User;


import com.example.YakServer.Models.Movie;

import java.util.List;

public class UserResponse {
    private String Response;
    private String Id;
    private String UserName;
    private String Email;
    private List<Movie> SavedMovies;

    UserResponse() {

    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public List<Movie> getSavedMovies() {
        return SavedMovies;
    }

    public void setSavedMovies(List<Movie> savedMovies) {
        SavedMovies = savedMovies;
    }
}
