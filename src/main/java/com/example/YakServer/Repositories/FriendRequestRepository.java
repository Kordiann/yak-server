package com.example.YakServer.Repositories;

import com.example.YakServer.Models.FriendRequest;
import com.example.YakServer.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
    Optional<FriendRequest> findBySenderAndRecipient(User sender, User recipient);
    Optional<List<FriendRequest>> findBySender(User sender);
    Optional<List<FriendRequest>> findByRecipient(User recipient);
}
