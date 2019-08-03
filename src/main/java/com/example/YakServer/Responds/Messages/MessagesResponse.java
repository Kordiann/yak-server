package com.example.YakServer.Responds.Messages;

import com.example.YakServer.Messages.SearchFor.MessagesHelpers.UserMessagesContainer;

import java.util.List;

public class MessagesResponse {
    private String Response;
    private String user;
    private List<UserMessagesContainer> messages;

    public MessagesResponse() {}

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<UserMessagesContainer> getMessages() {
        return messages;
    }

    public void setMessages(List<UserMessagesContainer> messages) {
        this.messages = messages;
    }
}
