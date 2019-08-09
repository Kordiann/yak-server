package com.example.YakServer.Messages;

import com.example.YakServer.Messages.SearchFor.MessagesHelpers.UserMessagesContainer;
import com.example.YakServer.Messages.SearchFor.SearchMessages;

import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.MessageRepository;

import com.example.YakServer.Repositories.UserRepository;
import com.example.YakServer.Responds.Messages.MessagesResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class MessagesSystem {
    private UserRepository userRepo;

    private SearchMessages searchMessages;

    private List<UserMessagesContainer> messagesContainers;

    private MessagesResponse messagesRes;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MessagesSystem(MessageRepository messageRepo, UserRepository userRepo) {
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

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messagesRes);
        } else {
            messagesRes.setResponse("400");

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messagesRes);
        }
    }
}
