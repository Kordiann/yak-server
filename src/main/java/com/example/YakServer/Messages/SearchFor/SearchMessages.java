package com.example.YakServer.Messages.SearchFor;

import com.example.YakServer.Messages.MessagesService;
import com.example.YakServer.Messages.SearchFor.MessagesHelpers.UserMessagesContainer;

import com.example.YakServer.Models.Message;
import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.MessageRepository;
import com.example.YakServer.Responds.SimplifiedModels.SMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchMessages extends MessagesService {
    private MessageRepository messageRepo;

    private List<UserMessagesContainer> messagesContainers;

    public SearchMessages(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    public void execute(List<UserMessagesContainer> messagesContainers, User user) {
        this.messagesContainers = messagesContainers;

        search(user);
    }

    private void search(User user) {
        Optional<List<Message>> optionalMessages =
                messageRepo.findAllBySenderOrAndRecipient(user, user);

        if(optionalMessages.isPresent()) {
            validate(simplifyMessage(optionalMessages.get()), user);
        } else {
            this.messagesContainers = new ArrayList<>();
        }
    }

    private void validate(List<SMessage> messages, User user){
        for(SMessage message :
                    messages) {
            String actualUser;

            if(message.getSender().equals(user.getUserName())) {
                actualUser = message.getRecipient();

                pushDownList(message, actualUser);
            } else if(message.getRecipient().equals(user.getUserName())) {
                actualUser = message.getSender();

                pushDownList(message, actualUser);
            }
        }
    }

    private void pushDownList(SMessage message, String actualUser) {
        if(this.messagesContainers.isEmpty()) {
            assignUser(message, actualUser);

        } else if(checkExisting(actualUser)) {
            this.messagesContainers.get(getIndex(actualUser)).getMessages().add(message);

        } else if(!checkExisting(actualUser)) {
            assignUser(message, actualUser);

        }
    }

    private int getIndex(String actualUser) {
        int x = 0;

        for(int i = 0; i < this.messagesContainers.size(); i++) {
            if(this.messagesContainers.get(i).getUserName().equals(actualUser)) {
                x = i;
            }
        }

        return x;
    }

    private void assignUser(SMessage message, String actualUser) {
        UserMessagesContainer messagesContainer = new UserMessagesContainer();

        messagesContainer.setUserName(actualUser);

        if(messagesContainer.getMessages() == null) {
            List<SMessage> messages = new ArrayList<>();

            messages.add(message);

            messagesContainer.setMessages(messages);
        } else {
            messagesContainer.getMessages().add(message);
        }

        this.messagesContainers.add(messagesContainer);
    }

    private boolean checkExisting(String actualUser) {
        for(UserMessagesContainer messagesContainer :
                    messagesContainers) {
            if(messagesContainer.getUserName().equals(actualUser)) {
                return true;
            }
        }

        return false;
    }
}
