package com.example.YakServer.Responds.Friends;

import com.example.YakServer.Models.User;

import java.util.List;

public class FriendResponse {
    private String Response;
    private List<User> Friends;

    private List<User> SenderFor;
    private List<User> RecipientBy;

    public FriendResponse() {
    }

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

    public List<User> getSenderFor() {
        return SenderFor;
    }

    public void setSenderFor(List<User> senderFor) {
        SenderFor = senderFor;
    }

    public List<User> getRecipientBy() {
        return RecipientBy;
    }

    public void setRecipientBy(List<User> recipientBy) {
        RecipientBy = recipientBy;
    }
}
