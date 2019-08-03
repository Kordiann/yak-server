package com.example.YakServer.User;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Responds.Users.AuthResponse;
import com.example.YakServer.Responds.Response;
import com.example.YakServer.Responds.Users.UserResponse;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.UserRepository;

import com.example.YakServer.Responds.Users.UsersResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    private final Gson gson = new Gson();

    private final ObjectMapper mapper = new ObjectMapper();

    //    ---------- POST METHODS ----------      //

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addUser (@RequestParam String userName, @RequestParam String password, @RequestParam String email) {
        EmailOperator operator = new EmailOperator(userRepo);

        User user = new User();

        user.setUserName(userName);
        user.setPassword(password);
        user.setActivationCode(operator.generateCode());
        user.setEmail(email);
        user.setDefaultUser(true);
        user.setSender(false);
        user.setRecipient(false);
        user.setFriend(false);

//        operator.sendEmail(email);

        userRepo.save(user);

        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping(path = "/user/change_password")
    public @ResponseBody
    String updatePassword (@RequestParam Integer userID, @RequestParam String oldPassword,
                              @RequestParam String newPassword) {
        Optional optionalUser = userRepo.findById(userID);
        Response res = new Response();

        if(optionalUser.isPresent()) {
            User user = (User) optionalUser.get();

            if(user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepo.save(user);

                res.setResponse("200");
            }else {
                res.setResponse("400");
            }

        } else {
            res.setResponse("400");
        }

        return gson.toJson(res);
    }

//    ---------- GET METHODS ----------      //

    @GetMapping(path = "/all")
    public @ResponseBody
    String allUsers () throws JsonProcessingException{
        UsersResponse res = new UsersResponse();

        List<User> users = Lists.newArrayList(userRepo.findAll());

        res.setResponse("200");
        res.setUsers(users);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
    }



    @GetMapping(path = "/authorize")
    public @ResponseBody
    String authorizeUser (@RequestParam String userName, @RequestParam String password) {
        AuthResponse response = new AuthResponse();

        User user = userRepo.findByUserName(userName).get();

        if(user.getUserName().equals(userName)
                && user.getPassword().equals(password)){
            response.setId(user.getId().toString());
            response.setResponse("200");
        } else {
            response.setId(null);
            response.setResponse("400");
        }

        return gson.toJson(response);
    }

    @GetMapping(path = "/user")
    public @ResponseBody
    String getUser (@RequestParam String userName, @RequestParam Integer userID)
            throws JsonProcessingException {
        UserResponse res = new UserResponse();

        Optional optionalUser = userRepo.findByUserName(userName);

        if(optionalUser.isPresent()) {
            User user = (User) optionalUser.get();

            if(user.getUserName().equals(userName)
                    && user.getId().equals(userID)){
                res.setUser(user);
                res.setResponse("200");
            } else {
                res.setUser(null);
                res.setResponse("400");
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
    }

    @GetMapping(path = "/user/movies")
    public @ResponseBody
    List<Movie> getUserMovies (@RequestParam Integer userID) {
        return new UserService(userRepo).getUserMovies(userID);
    }

    @GetMapping(path = "/user/test")
    public @ResponseBody
    void sendEmail () throws UnirestException {
        new EmailOperator(userRepo).sendSimpleMessage();
    }


//    @GetMapping(path = "/activate")
//    public @ResponseBody
//    String activateEmail (@RequestParam Integer code) {
//        EmailOperator operator = new EmailOperator(userRepository);
//        Gson gson = new Gson();
//        Response res = new Response();
//
//        operator.activate(code);
//
//        if (operator.activate(code)) {
//            res.setResponse("200");
//        } else {
//            res.setResponse("400");
//        }
//
//        return gson.toJson(res);
//    }

//    @GetMapping(path = "/send")
//    public @ResponseBody
//    void send (@RequestParam String email) {
//        EmailOperator operator = new EmailOperator(userRepository);
//        operator.sendEmail(email);
//    }


}
