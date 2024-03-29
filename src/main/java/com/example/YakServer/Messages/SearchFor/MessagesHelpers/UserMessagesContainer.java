package com.example.YakServer.Messages.SearchFor.MessagesHelpers;

import com.example.YakServer.Responds.SimplifiedModels.SMessage;

import java.util.List;

public class UserMessagesContainer {
    private String userName;
    private Integer userID;
    private List<SMessage> messages;

    public UserMessagesContainer() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<SMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SMessage> messages) {
        this.messages = messages;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
