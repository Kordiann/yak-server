package com.example.YakServer.User;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(Integer id) {
        if (userRepository.findById(id).isPresent()) return userRepository.findById(id).get();
        else return new User();
    }

    public List<Movie> getUserMovies(Integer userID) {
        Optional optionalUser = userRepository.findById(userID);
        if(optionalUser.isPresent()) {
            User user = (User) optionalUser.get();

            return user.getSavedMovies();
        } else return new ArrayList<>();

    }
}
