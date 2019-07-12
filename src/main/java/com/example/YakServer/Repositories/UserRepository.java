package com.example.YakServer.Repositories;

import com.example.YakServer.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User, Integer> {
    User findById(int id);
    User findByUserName(String userName);
}
