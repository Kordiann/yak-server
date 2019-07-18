package com.example.YakServer.Repositories;

import com.example.YakServer.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository  extends CrudRepository<User, Integer> {
    User findById(int id);
    Optional<User> findByUserName(String userName);
    User findByActivationCode(Integer activationCode);
}
