package com.example.demo.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="messageBox")
public class MessagesBox {

    @Id
    @NotNull
    private Integer Id;

    @OneToOne
    @JoinColumn(name = "owner")
    private User Owner;

    @OneToMany(mappedBy = "messagesBox")
    private List<Message> messages;

    public MessagesBox(User owner) {
        this.Owner = owner;
    }

    public MessagesBox() {}

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public User getOwner() {
        return Owner;
    }

    public void setOwner(User owner) {
        Owner = owner;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
