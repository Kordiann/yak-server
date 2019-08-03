package com.example.YakServer.Messages;

import com.example.YakServer.Messages.SearchFor.MessagesHelpers.UserMessagesContainer;
import com.example.YakServer.Messages.SearchFor.SearchMessages;

import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MessageRepository;

import com.example.YakServer.Repositories.UserRepository;
import com.example.YakServer.Responds.Messages.MessagesResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class MessagesSystem {
    private MessageRepository messageRepo;

    private UserRepository userRepo;

    private SearchMessages searchMessages;

    private List<UserMessagesContainer> messagesContainers;

    private MessagesResponse messagesRes;

    private final ObjectMapper mapper = new ObjectMapper();

    MessagesSystem(MessageRepository messageRepo, UserRepository userRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;

        this.searchMessages = new SearchMessages(messageRepo);
    }

    String genForMessagesPage(Integer userID) throws JsonProcessingException {
        Optional<User> optionalUser = userRepo.findById(userID);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            messagesRes = new MessagesResponse();

            this.messagesContainers = new ArrayList<>();

            searchMessages.execute(messagesContainers, user);

            messagesRes.setResponse("200");
            messagesRes.setUser(user.getUserName());
            messagesRes.setMessages(messagesContainers);

//            System.out.println(messagesContainers);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messagesRes);
        } else {
            messagesRes.setResponse("400");

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messagesRes);
        }
    }
}
