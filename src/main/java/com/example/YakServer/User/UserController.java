package com.example.YakServer.User;

import com.example.YakServer.Models.Movie;
import com.example.YakServer.Responds.AuthResponse;
import com.example.YakServer.Responds.Response;
import com.example.YakServer.Responds.UserResponse;
import com.example.YakServer.Models.Message;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MessageRepository;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;

import com.example.YakServer.Responds.UsersResponse;
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
    private UserRepository userRepository;

    @Autowired
    public MovieRepository movieRepository;

    @Autowired
    public MessageRepository messageRepository;

    private final Gson gson = new Gson();

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addUser (@RequestParam String userName, @RequestParam String password, @RequestParam String email) {
        EmailOperator operator = new EmailOperator(userRepository);

        User user = new User();

        user.setUserName(userName);
        user.setPassword(password);
        user.setActivationCode(operator.generateCode());
        user.setEmail(email);
        user.setDefaultUser(true);

//        operator.sendEmail(email);

        userRepository.save(user);

        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }



    @PostMapping(path = "/user/change_password")
    public @ResponseBody
    String updatePassword (@RequestParam Integer userID, @RequestParam String oldPassword,
                              @RequestParam String newPassword) {
        Optional optionalUser = userRepository.findById(userID);
        Response res = new Response();

        if(optionalUser.isPresent()) {
            User user = (User) optionalUser.get();

            if(user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);

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

        List<User> users = Lists.newArrayList(userRepository.findAll());

        res.setResponse("200");
        res.setUsers(users);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
    }

    @GetMapping(path = "/message/all")
    public @ResponseBody
    Iterable<Message> allMessages () {
        return messageRepository.findAll();
    }

    @GetMapping(path = "/authorize")
    public @ResponseBody
    String authorizeUser (@RequestParam String userName, @RequestParam String password) {
        AuthResponse response = new AuthResponse();

        User user = userRepository.findByUserName(userName).get();

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

        Optional optionalUser = userRepository.findByUserName(userName);

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
        return new UserService(userRepository).getUserMovies(userID);
    }

    @GetMapping(path = "/user/test")
    public @ResponseBody
    void sendEmail () throws UnirestException {
        new EmailOperator(userRepository).sendSimpleMessage();
    }

    @GetMapping(path = "/baba")
    public @ResponseBody
    void sada () {
        List<User> users = Lists.newArrayList(userRepository.findAll());

        for (User user :
                    users) {
            user.setDefaultUser(true);
            userRepository.save(user);
        }


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

//    @PostMapping(path = "/message/post")
//    public @ResponseBody
//    String createChatMessage (@RequestParam Integer authorId, @RequestParam Integer recipientId,
//                              @RequestParam String content) {
//
//        Iterable<User> Users = userRepository.findAll();
//
//        Date date =  new Date();
//
//        Message message = new Message();
//
//        User author = new User();
//        User recipient = new User();
//
//        for (User user :
//                Users) {
//            if(user.getId().equals(authorId)) author = user;
//            else if(user.getId().equals(recipientId)) recipient = user;
//        }
//
//        message.setAuthorUser(author);
//        message.setRecipientUser(recipient);
//        message.setTimeSend(date);
//        message.setContent(content);
//
//        messageRepository.save(message);
//
//        return "Posted Message from " + author.getUserName() + " To " + recipient.getUserName();
//    }
}
