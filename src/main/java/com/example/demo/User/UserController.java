package com.example.demo.User;

import com.example.demo.Models.Message;
import com.example.demo.Models.User;
import com.example.demo.Repositories.MessageRepository;
import com.example.demo.Repositories.MovieRepository;
import com.example.demo.Repositories.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/user")
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

        User user = new User();

        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);

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
            Response response = new Response();

            User user = userRepository.findByUserName(userName);
            //TODO use password to authorize!!!

            if (user == null) {
                response.setId(null);
                response.setResponse("400");
            } else {
                response.setId(user.getId().toString());
                response.setResponse("200");
            }

//            for (User user :
//                    Users) {
//                if (user.getUserName().equals(userName)) userToCheck = user;
//            }
//
//            if(userToCheck.equals(null)) {
//                response.setResponse("400");
//            } else {
//                if (userToCheck.getUserName().equals(userName) && userToCheck.getPassword().equals(password)) {
//                    response.setId(userToCheck.getId().toString());
//                    response.setResponse("200");
//                } else {
//                    response.setResponse("400");
//                }
//            }

            return gson.toJson(response);
    }

}
