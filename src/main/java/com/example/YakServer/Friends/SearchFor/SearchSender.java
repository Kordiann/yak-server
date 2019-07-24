package com.example.YakServer.Friends.SearchFor;

import com.example.YakServer.Models.FriendRequest;
import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.FriendRequestRepository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SearchSender {
    private FriendRequestRepository friendReqRepo;

    private List<User> users;

    private List<User> recipients;

    public SearchSender(FriendRequestRepository friendReqRepo) {
        this.friendReqRepo = friendReqRepo;
    }

    public void execute(List<User> users, User user) {
        this.users = users;

        search(user);

        generateResult();
    }

    private void generateResult() {
        for (User user :
                users) {
            if(user.getDefaultUser()) {
                for (User recipient :
                        recipients) {
                    if (recipient.getId().equals(user.getId())) {
                        user.setRecipient(true);
                        user.setDefaultUser(false);
                    }
                }
            }
        }
    }

    private void search(User user) {
        Optional<List<FriendRequest>> optionalRecipients =
                friendReqRepo.findBySender(user);

        if(optionalRecipients.isPresent()) {
            this.recipients = validate(optionalRecipients.get());
        } else {
            this.recipients = new ArrayList<>();
        }
    }


    private List<User> validate(List<FriendRequest> friendRequests) {
        List<User> recipients = new ArrayList<>();

        for(FriendRequest friendRequest :
                    friendRequests) {
            if(!friendRequest.isActivate()) {
                recipients.add(friendRequest.getRecipient());
            }
        }

        return recipients;
    }
}
