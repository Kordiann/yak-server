package com.example.YakServer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    private String userName;
    private String password;

    //TODO Activate email system
    private boolean isActivate;
    private boolean isActivationCodeSend;
    private Integer activationCode;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "user_movies",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "movie_id") } )
    @JsonManagedReference
    private List<Movie> SavedMovies;

    @javax.validation.constraints.Email
    private String Email;

    private Boolean isFriend;

    private Boolean isSender;

    private Boolean isRecipient;

    private Boolean isDefaultUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @JsonIgnore
    public List<Movie> getSavedMovies() {
        return SavedMovies;
    }

    public void setSavedMovies(List<Movie> savedMovies) {
        SavedMovies = savedMovies;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public boolean isActivationCodeSend() {
        return isActivationCodeSend;
    }

    public void setActivationCodeSend(boolean activationCodeSend) {
        isActivationCodeSend = activationCodeSend;
    }

    public Integer getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(Integer activationCode) {
        this.activationCode = activationCode;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public Boolean getSender() {
        return isSender;
    }

    public void setSender(Boolean sender) {
        isSender = sender;
    }

    public Boolean getRecipient() {
        return isRecipient;
    }

    public void setRecipient(Boolean recipient) {
        isRecipient = recipient;
    }

    public Boolean getDefaultUser() {
        return isDefaultUser;
    }

    public void setDefaultUser(Boolean defaultUser) {
        isDefaultUser = defaultUser;
    }
}
