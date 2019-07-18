package com.example.YakServer.Models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "friendbox")
public class FriendBox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    private User owner;

    @OneToMany
    private List<Friend> friends;

    FriendBox(User owner) { this.owner = owner; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
