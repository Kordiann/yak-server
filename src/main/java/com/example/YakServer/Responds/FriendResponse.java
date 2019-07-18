package com.example.YakServer.Responds;

import com.example.YakServer.Models.User;

import java.util.List;

public class FriendResponse {
    private String Response;
    private List<User> Friends;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<User> getFriends() {
        return Friends;
    }

    public void setFriends(List<User> friends) {
        Friends = friends;
    }
}
