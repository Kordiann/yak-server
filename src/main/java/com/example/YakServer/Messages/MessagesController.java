package com.example.YakServer.Messages;

import com.example.YakServer.Models.Message;
import com.example.YakServer.Models.User;

import com.example.YakServer.Movies.MovieSystem.DBGenerator;
import com.example.YakServer.Repositories.MessageRepository;
import com.example.YakServer.Repositories.UserRepository;

import com.example.YakServer.Responds.Messages.MessagesResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/messages")
public class MessagesController {
    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;

    private final Logger logger = Logger.getLogger(MessagesController.class.getName());


    //    ---------- POST METHODS ----------      //

    @PostMapping(path = "/post")
    public @ResponseBody
    void pushMessage (@RequestParam Integer senderID, @RequestParam Integer recipientID,
                              @RequestParam String content) {
        Optional<User> sender = userRepo.findById(senderID);
        Optional<User> recipient = userRepo.findById(recipientID);

        if (sender.isPresent() &&
                recipient.isPresent()) {
            Message message = new Message();
            Date date = new Date();

            message.setSender(sender.get());
            message.setRecipient(recipient.get());
            message.setContent(content);

            message.setTimeSend(date);

            messageRepo.save(message);
        } else {
            logger.log(Level.WARNING, "Sender or Recipient does not exists");
        }
    }

    //    ---------- GET METHODS ----------      //

    @GetMapping(path = "/get/all")
    public @ResponseBody
    String getMessages (@RequestParam Integer senderID)
            throws JsonProcessingException {
        MessagesSystem messagesSystem = new MessagesSystem(messageRepo, userRepo);
        return messagesSystem.genForMessagesPage(senderID);
    }

//    @GetMapping(path = "/get")
//    public @ResponseBody
//    String getMessages (@RequestParam Integer senderID, @RequestParam Integer recipientID)
//            throws JsonProcessingException {
//        MessagesSystem messagesSystem = new MessagesSystem(messageRepo, userRepo);
//        return messagesSystem.genForMessagesPage(senderID);
//    }
}
