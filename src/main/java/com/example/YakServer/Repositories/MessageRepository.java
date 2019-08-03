package com.example.YakServer.Repositories;

import com.example.YakServer.Models.Message;
import com.example.YakServer.Models.User;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<List<Message>> findAllBySenderOrAndRecipient(User Sender, User Recipient);
}
