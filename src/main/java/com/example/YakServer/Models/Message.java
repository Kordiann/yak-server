package com.example.YakServer.Models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @javax.persistence.Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @OneToOne
    @JoinColumn(name = "authorUserId")
    private User AuthorUser;

    @OneToOne
    @JoinColumn(name = "recipientUserId")
    private User RecipientUser;

    @NotNull
    private Date TimeSend;

    @NotNull
    @NaturalId
    private String Content;

    @ManyToOne
    private MessagesBox messagesBox;

    public Message() {}

    public Message(User authorUser, User recipientUser, String content) {
        this.AuthorUser = authorUser;
        this.RecipientUser = recipientUser;
        this.Content = content;
        this.TimeSend = new Date();
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public User getAuthorUser() {
        return AuthorUser;
    }

    public void setAuthorUser(User authorUser) {
        AuthorUser = authorUser;
    }

    public User getRecipientUser() {
        return RecipientUser;
    }

    public void setRecipientUser(User recipientUser) {
        RecipientUser = recipientUser;
    }

    public Date getTimeSend() {
        return TimeSend;
    }

    public void setTimeSend(Date timeSend) {
        TimeSend = timeSend;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public MessagesBox getMessagesBox() {
        return messagesBox;
    }

    public void setMessagesBox(MessagesBox messagesBox) {
        this.messagesBox = messagesBox;
    }
}
