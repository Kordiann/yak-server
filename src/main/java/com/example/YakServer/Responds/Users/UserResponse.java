package com.example.YakServer.Responds.Users;

import com.example.YakServer.Models.User;

public class UserResponse {
    private String Response;
    private User user;

    public UserResponse() {

    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
