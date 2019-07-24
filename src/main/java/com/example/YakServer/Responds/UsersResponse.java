package com.example.YakServer.Responds;

import com.example.YakServer.Models.User;

import java.util.List;

public class UsersResponse {
    private String Response;
    private List<User> Users;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }
}
