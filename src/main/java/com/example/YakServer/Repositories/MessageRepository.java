package com.example.YakServer.Repositories;

import com.example.YakServer.Models.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
