package com.example.YakServer.Friends.SearchFor;

import com.example.YakServer.Models.FriendRequest;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.FriendRequestRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SearchRecipient {
    private FriendRequestRepository friendReqRepo;

    private List<User> users;

    private List<User> senders;

    public SearchRecipient(FriendRequestRepository friendReqRepo) {
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
                        senders) {
                    if (recipient.getId().equals(user.getId())) {
                        user.setSender(true);
                        user.setDefaultUser(false);
                    }
                }
            }
        }
    }

    private void search(User user) {
        Optional<List<FriendRequest>> optionalFriendRequests =
                friendReqRepo.findByRecipient(user);

        if(optionalFriendRequests.isPresent()) {
            this.senders = validate(optionalFriendRequests.get());
        } else {
            this.senders = new ArrayList<>();
        }
    }

    private List<User> validate(List<FriendRequest> friendRequests) {
        List<User> senders = new ArrayList<>();

        for(FriendRequest friendRequest :
                friendRequests) {
            if(!friendRequest.isActivate()) {
                senders.add(friendRequest.getSender());
            }
        }

        return senders;
    }
}
