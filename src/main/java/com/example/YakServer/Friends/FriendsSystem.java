package com.example.YakServer.Friends;

import com.example.YakServer.Friends.SearchFor.SearchRecipient;
import com.example.YakServer.Friends.SearchFor.SearchSender;
import com.example.YakServer.Friends.SearchFor.SearchFriends;

import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.FriendRepository;
import com.example.YakServer.Repositories.FriendRequestRepository;
import com.example.YakServer.Repositories.UserRepository;

import com.example.YakServer.Responds.UsersResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class FriendsSystem {
    private List<User> users;

    private UserRepository userRepo;

    private SearchFriends searchFriends;
    private SearchSender searchSender;
    private SearchRecipient searchRecipient;

    private final ObjectMapper mapper = new ObjectMapper();

    private UsersResponse usersRes = new UsersResponse();


    FriendsSystem(UserRepository userRepo,
                  FriendRepository friendRepo,
                  FriendRequestRepository friendReqRepo) {
        this.userRepo = userRepo;

        this.searchFriends = new SearchFriends(friendRepo);
        this.searchSender = new SearchSender(friendReqRepo);
        this.searchRecipient = new SearchRecipient(friendReqRepo);
    }

    String genForFriendsPage(Integer userID) throws JsonProcessingException {
        Optional<User> optionalUser = userRepo.findById(userID);

        this.users = preSetUsers(Lists.newArrayList(userRepo.findAll()), userID);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            searchFriends.execute(users, user);
            searchRecipient.execute(users, user);
            searchSender.execute(users, user);

            deleteDefaultUsers(users);

            usersRes.setResponse("200");
            usersRes.setUsers(users);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersRes);
        } else {
            usersRes.setResponse("400");
            usersRes.setUsers(null);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersRes);
        }
    }

    String genForUserPage(Integer userID) throws JsonProcessingException {
        Optional<User> optionalUser = userRepo.findById(userID);

        this.users = preSetUsers(Lists.newArrayList(userRepo.findAll()), userID);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            searchFriends.execute(users, user);
            searchRecipient.execute(users, user);
            searchSender.execute(users, user);

            usersRes.setResponse("200");
            usersRes.setUsers(users);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersRes);
        } else {
            usersRes.setResponse("400");
            usersRes.setUsers(null);

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersRes);
        }
    }

    private List<User> preSetUsers(List<User> users, Integer userID) {
        users.removeIf(user -> (user.getId().equals(userID)));

        return users;
    }

    private void deleteDefaultUsers(List<User> users) {
        users.removeIf(user -> (user.getDefaultUser()));

        this.users = users;
    }
}
