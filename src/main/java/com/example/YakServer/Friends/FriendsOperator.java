package com.example.YakServer.Friends;

import com.example.YakServer.Models.Friend;
import com.example.YakServer.Models.FriendRequest;
import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.FriendRepository;
import com.example.YakServer.Repositories.FriendRequestRepository;

import com.example.YakServer.Responds.FriendReqResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class FriendsOperator {
    private FriendRequestRepository friendReqRepo;

    private FriendRepository friendRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    private final FriendReqResponse friendReqRes = new FriendReqResponse();

    @Autowired
    FriendsOperator(FriendRequestRepository friendReqRepo, FriendRepository friendRepo) {
        this.friendRepo = friendRepo;
        this.friendReqRepo = friendReqRepo;
    }

    String push(User sender, User recipient) throws JsonProcessingException {
        if(!reqExists(sender, recipient)) {
            FriendRequest friendRequest = new FriendRequest();

            friendRequest.setSender(sender);
            friendRequest.setRecipient(recipient);

            friendReqRepo.save(friendRequest);

            friendReqRes.setRecipientName(recipient.getUserName());
            friendReqRes.setSenderName(sender.getUserName());

            friendReqRes.setResponse("200");
        } else {
            friendReqRes.setResponse("400");
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(friendReqRes);
    }

    String activate(User sender, User recipient) throws JsonProcessingException {
        Optional<FriendRequest> optionalFriendRequest =
                friendReqRepo.findBySenderAndRecipient(sender, recipient);

        if(optionalFriendRequest.isPresent()) {
            FriendRequest friendRequest = optionalFriendRequest.get();
            if(!friendRequest.isActivate()) {
                Friend friend = new Friend();
                friend.setFirstUser(sender);
                friend.setSecondUser(recipient);

                friendRepo.save(friend);

                friendRequest.setActivate(true);
                friendReqRepo.save(friendRequest);

                friendReqRes.setResponse("200");
                friendRequest.setActivate(true);
            } else {
                friendReqRes.setResponse("400");
                friendRequest.setActivate(true);
            }
        } else {
            friendReqRes.setResponse("koko");
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(friendReqRes);
    }

    String delete(User sender, User recipient) throws JsonProcessingException {
        Optional<FriendRequest> optionalFriendRequest =
                friendReqRepo.findBySenderAndRecipient(sender, recipient);

        if(optionalFriendRequest.isPresent()) {
            FriendRequest friendRequest = optionalFriendRequest.get();
            if(!friendRequest.isActivate()) {
                friendReqRepo.delete(friendRequest);

                friendReqRes.setResponse("200");
            } else {
                friendReqRes.setResponse("400");
            }
        } else {
            friendReqRes.setResponse("400");
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(friendReqRes);
    }

    private Boolean reqExists(User sender, User recipient) {
        Optional<FriendRequest> optionalReqFirst =
                friendReqRepo.findBySenderAndRecipient(sender, recipient);

        Optional<FriendRequest> optionalReqSecond =
                friendReqRepo.findBySenderAndRecipient(recipient, sender);

        return optionalReqFirst.isPresent() && optionalReqSecond.isPresent();
    }
}
