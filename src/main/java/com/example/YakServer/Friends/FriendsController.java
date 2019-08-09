package com.example.YakServer.Friends;

import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.FriendRepository;
import com.example.YakServer.Repositories.FriendRequestRepository;
import com.example.YakServer.Repositories.UserRepository;
import com.example.YakServer.Responds.Friends.FriendReqResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "https://yak-client.herokuapp.com")
@RequestMapping(path = "/friends")
public class FriendsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    private final Gson gson = new Gson();

    //    ---------- POST METHODS ----------      //

    @PostMapping(path = "/push")
    public @ResponseBody
    String pushFriendRequest(@RequestParam String senderName, @RequestParam String recipientName)
            throws JsonProcessingException {
        Optional<User> optionalSender = userRepository.findByUserName(senderName);
        Optional<User> optionalRecipient = userRepository.findByUserName(recipientName);

        FriendReqResponse res = new FriendReqResponse();


        if (optionalSender.isPresent() && optionalRecipient.isPresent()) {
            return new FriendsOperator(friendRequestRepository, friendRepository)
                    .push(optionalSender.get(), optionalRecipient.get());

        } else {
            res.setResponse("400");
            return gson.toJson(res);
        }
    }

    @PostMapping(path = "/activate")
    public @ResponseBody
    String activateFriend(@RequestParam String senderName, @RequestParam String recipientName)
            throws JsonProcessingException {
        FriendReqResponse res = new FriendReqResponse();

        Optional<User> sender = userRepository.findByUserName(senderName);
        Optional<User> recipient = userRepository.findByUserName(recipientName);

        if(sender.isPresent() && recipient.isPresent()) {
            return new FriendsOperator(friendRequestRepository, friendRepository)
                    .activate(sender.get(), recipient.get());
        } else {
            res.setResponse("400");
            return gson.toJson(res);
        }
    }

    //    ---------- GET METHODS ----------      //

    @GetMapping(path="/userFriends")
    public @ResponseBody
    String getUserFriends(@RequestParam Integer userID) throws JsonProcessingException {
        return new FriendsSystem(userRepository, friendRepository, friendRequestRepository)
                .genForFriendsPage(userID);
    }

    @GetMapping(path = "/usersForUsersPage")
    public @ResponseBody
    String getUsersForUsersPage(@RequestParam Integer userID)
            throws JsonProcessingException {
        return new FriendsSystem(userRepository, friendRepository, friendRequestRepository)
                .genForUserPage(userID);
    }

    //    ---------- DELETE METHODS ----------      //

    @DeleteMapping(path = "/deleteReq")
    public @ResponseBody
    String deleteFriendRequest(@RequestParam String senderName, @RequestParam String recipientName)
            throws JsonProcessingException{
        Optional<User> sender = userRepository.findByUserName(senderName);
        Optional<User> recipient = userRepository.findByUserName(recipientName);

        FriendReqResponse res = new FriendReqResponse();

        if(sender.isPresent() && recipient.isPresent()) return
                new FriendsOperator(friendRequestRepository, friendRepository)
                        .delete(sender.get(), recipient.get());
        else {
            res.setResponse("400");
            return gson.toJson(res);
        }
    }
}
