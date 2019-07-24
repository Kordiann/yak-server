package com.example.YakServer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    private User firstUser;

    @OneToOne
    private User secondUser;

    @NotNull
    private Date timeExists;

    @ManyToOne
    private FriendBox friendBox;

    public Friend() {
        this.timeExists = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    @JsonIgnore
    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public Date getTimeExists() {
        return timeExists;
    }

    public void setTimeExists(Date timeExists) {
        this.timeExists = timeExists;
    }

    public FriendBox getFriendBox() {
        return friendBox;
    }

    public void setFriendBox(FriendBox friendBox) {
        this.friendBox = friendBox;
    }
}
