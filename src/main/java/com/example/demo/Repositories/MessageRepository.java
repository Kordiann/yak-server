package com.example.demo.Repositories;

import com.example.demo.Models.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
