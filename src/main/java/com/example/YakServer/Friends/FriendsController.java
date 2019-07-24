package com.example.YakServer.Friends;

import com.example.YakServer.Models.Friend;
import com.example.YakServer.Models.FriendRequest;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.FriendRepository;
import com.example.YakServer.Repositories.FriendRequestRepository;
import com.example.YakServer.Repositories.UserRepository;
import com.example.YakServer.Responds.FriendReqResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/friends")
public class FriendsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    private final Gson gson = new Gson();

    private final ObjectMapper mapper = new ObjectMapper();

    //    ---------- POST METHODS ----------      //

    @PostMapping(path="/push")
    public @ResponseBody
    String pushFriendRequest(@RequestParam String senderName, @RequestParam String recipientName) {
        FriendReqResponse res = new FriendReqResponse();

        Optional<User> optionalSender = userRepository.findByUserName(senderName);
        Optional<User> optionalRecipient = userRepository.findByUserName(recipientName);

        if (optionalSender.isPresent() && optionalRecipient.isPresent()) {
            Optional<FriendRequest> optionalFriendRequest1 =
                    friendRequestRepository.findBySenderAndRecipient(optionalSender.get(), optionalRecipient.get());
            Optional<FriendRequest> optionalFriendRequest2 =
                    friendRequestRepository.findBySenderAndRecipient(optionalRecipient.get(), optionalSender.get());

            if (!optionalFriendRequest1.isPresent() &&
            !optionalFriendRequest2.isPresent()) {
                FriendRequest friendRequest = new FriendRequest();

                friendRequest.setSender(optionalSender.get());
                friendRequest.setRecipient(optionalRecipient.get());

                friendRequestRepository.save(friendRequest);

                res.setRecipientName(optionalRecipient.get().getUserName());
                res.setSenderName(optionalSender.get().getUserName());

                res.setResponse("200");
            } else {
                res.setResponse("400");
            }
        } else {
            res.setResponse("400");
        }

        return gson.toJson(res);
    }

    @PostMapping(path="/activate")
    public @ResponseBody
    String activateFriend(@RequestParam String senderName, @RequestParam String recipientName) {
        FriendReqResponse res = new FriendReqResponse();

        Optional<User> sender = userRepository.findByUserName(senderName);
        Optional<User> recipient = userRepository.findByUserName(recipientName);

        if(sender.isPresent() && recipient.isPresent()) {
            Optional<FriendRequest> optionalFriendRequest =
                    friendRequestRepository.findBySenderAndRecipient(sender.get(), recipient.get());

            if(optionalFriendRequest.isPresent()) {
                if(!optionalFriendRequest.get().isActivate()) {

                    if (optionalFriendRequest.get()
                            .getRecipient()
                            .getId()
                            .equals(recipient.get().getId())) {
                        Friend friend = new Friend();
                        FriendRequest friendRequest = optionalFriendRequest.get();
                        friendRequest.setActivate(true);

                        friend.setFirstUser(sender.get());
                        friend.setSecondUser(recipient.get());

                        friendRepository.save(friend);
                        friendRequestRepository.save(friendRequest);

                        res.setResponse("200");
                        res.setActivate(true);
                    } else {
                        res.setResponse("400");
                    }
                } else {
                    res.setResponse("400");
                    res.setActivate(true);
                }
            } else {
                res.setResponse("400");
            }
        } else {
            res.setResponse("400");
        }

        return gson.toJson(res);
    }

    //    ---------- GET METHODS ----------      //

    @GetMapping(path="/userFriends")
    public @ResponseBody
    String getUserFriends(@RequestParam Integer userID) throws JsonProcessingException {
        return new FriendsSystem(userRepository,friendRepository,friendRequestRepository)
                .genForFriendsPage(userID);
    }

    @GetMapping(path="/usersForUsersPage")
    public @ResponseBody
    String getUsersForUsersPage(@RequestParam Integer userID)
            throws JsonProcessingException {
        return new FriendsSystem(userRepository,
                friendRepository,
                friendRequestRepository).genForUserPage(userID);
    }
}
