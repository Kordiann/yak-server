package com.example.YakServer.Repositories;

import com.example.YakServer.Models.Friend;
import com.example.YakServer.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends CrudRepository<Friend, Integer> {
    Optional<List<Friend>> findAllByFirstUserOrAndSecondUser(User firstUser, User secondUser);
}
