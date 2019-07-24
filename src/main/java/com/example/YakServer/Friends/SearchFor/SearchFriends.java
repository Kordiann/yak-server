package com.example.YakServer.Friends.SearchFor;

import com.example.YakServer.Models.Friend;
import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.FriendRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SearchFriends {
    private FriendRepository friendRepo;

    private List<User> users;

    private List<User> friends;

    public SearchFriends(FriendRepository friendRepo) {
        this.friendRepo = friendRepo;
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
                for (User friend :
                        friends) {
                    if (friend.getId().equals(user.getId())) {
                        user.setFriend(true);
                        user.setDefaultUser(false);
                    }
                }
            }
        }
    }

    private void search(User user) {
        Optional<List<Friend>> optionalFriends =
                friendRepo.findAllByFirstUserOrAndSecondUser(user, user);

        if(optionalFriends.isPresent()) {
            this.friends = validate(optionalFriends.get(), user);
        } else {
            this.friends = new ArrayList<>();
        }
    }

    private List<User> validate(List<Friend> friends, User user){
        List<User> result = new ArrayList<>();

        for (Friend friend :
                friends) {
            if(friend.getFirstUser().getId().equals(user.getId())) {
                result.add(friend.getSecondUser());
            } else if(friend.getSecondUser().getId().equals(user.getId())) {
                result.add(friend.getFirstUser());
            }
        }

        return result;
    }
}
