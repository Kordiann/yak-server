package com.example.YakServer.User;

import com.example.YakServer.Responds.AuthResponse;
import com.example.YakServer.Responds.Response;
import com.example.YakServer.Responds.UserResponse;
import com.example.YakServer.Models.Message;
import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MessageRepository;
import com.example.YakServer.Repositories.MovieRepository;
import com.example.YakServer.Repositories.UserRepository;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addUser (@RequestParam String userName, @RequestParam String password, @RequestParam String email) {
        EmailOperator operator = new EmailOperator(userRepository);

        User user = new User();

        user.setUserName(userName);
        user.setPassword(password);
        user.setActivationCode(operator.generateCode());
        user.setEmail(email);

        operator.sendEmail(email);

        userRepository.save(user);

        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @PostMapping(path = "/message/post")
    public @ResponseBody
    String createChatMessage (@RequestParam Integer authorId, @RequestParam Integer recipientId,
                              @RequestParam String content) {

        Iterable<User> Users = userRepository.findAll();

        Date date =  new Date();

        Message message = new Message();

        User author = new User();
        User recipient = new User();

        for (User user :
                Users) {
            if(user.getId().equals(authorId)) author = user;
            else if(user.getId().equals(recipientId)) recipient = user;
        }

        message.setAuthorUser(author);
        message.setRecipientUser(recipient);
        message.setTimeSend(date);
        message.setContent(content);

        messageRepository.save(message);

        return "Posted Message from " + author.getUserName() + " To " + recipient.getUserName();
    }

    @PostMapping(path = "/user/change_password")
    public @ResponseBody
    String updatePassword (@RequestParam Integer userID, @RequestParam String oldPassword,
                              @RequestParam String newPassword) {
        return "XD";
    }

//    ---------- GET METHODS ----------      //

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> allUsers () {
        return userRepository.findAll();
    }

    @GetMapping(path = "/message/all")
    public @ResponseBody
    Iterable<Message> allMessages () {
        return messageRepository.findAll();
    }

    @GetMapping(path = "/authorize")
    public @ResponseBody
    String authorizeUser (@RequestParam String userName, @RequestParam String password) {
            Gson gson = new Gson();
            AuthResponse response = new AuthResponse();

            User user = userRepository.findByUserName(userName);

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
    String getUser (@RequestParam String userName, @RequestParam Integer userID) {
        Gson gson = new Gson();
        UserResponse response = new UserResponse();

        User user = userRepository.findByUserName(userName);

        if(user.getUserName().equals(userName)
                && user.getId().equals(userID)){
            response.setId(user.getId().toString());
//            response.setSavedMovies(user.getSavedMovies());
            response.setActivate(user.isActivate());
            response.setActivationCodeSend(user.isActivationCodeSend());
            response.setEmail(user.getEmail());
            response.setResponse("200");
        } else {
            response.setId(null);
            response.setResponse("400");
        }

        return gson.toJson(response);
    }

    @GetMapping(path = "/activate")
    public @ResponseBody
    String activateEmail (@RequestParam Integer code) {
        EmailOperator operator = new EmailOperator(userRepository);
        Gson gson = new Gson();
        Response res = new Response();

        operator.activate(code);

        if (operator.activate(code)) {
            res.setResponse("200");
        } else {
            res.setResponse("400");
        }

        return gson.toJson(res);
    }

    @GetMapping(path = "/send")
    public @ResponseBody
    void send (@RequestParam String email) {
        EmailOperator operator = new EmailOperator(userRepository);
        operator.sendEmail(email);
    }
}
