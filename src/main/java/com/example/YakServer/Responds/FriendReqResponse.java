package com.example.YakServer.Responds;

import com.example.YakServer.Models.User;

import java.util.List;

public class FriendReqResponse {
    private String Response;

    private List<User> RecipientsFrom;
    private List<User> SenderTo;

    private String RecipientName;
    private String SenderName;

    private boolean Activate;

    public FriendReqResponse() { }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public List<User> getRecipientsFrom() {
        return RecipientsFrom;
    }

    public void setRecipientsFrom(List<User> RecipientsFrom) {
        this.RecipientsFrom = RecipientsFrom;
    }

    public List<User> getSenderTo() {
        return SenderTo;
    }

    public void setSenderTo(List<User> SenderTo) {
        this.SenderTo = SenderTo;
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public void setRecipientName(String recipientName) {
        RecipientName = recipientName;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public boolean isActivate() {
        return Activate;
    }

    public void setActivate(boolean activate) {
        Activate = activate;
    }
}
