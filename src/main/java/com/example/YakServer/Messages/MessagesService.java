package com.example.YakServer.Messages;

import com.example.YakServer.Models.Message;
import com.example.YakServer.Responds.SimplifiedModels.SMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessagesService {
    List<Message> sortMessagesByDate(List<Message> mess1, List<Message> mess2) {
        List<Message> result = new ArrayList<>();

        result.addAll(mess1);
        result.addAll(mess2);

        result.sort(Comparator.comparing(Message::getTimeSend));
        return result;
    }

    protected List<SMessage> simplifyMessage(List<Message> messages) {
        List<SMessage> sMessageList = new ArrayList<>();

        for(Message message :
                    messages) {
            SMessage sMessage = new SMessage();

            sMessage.setId(message.getId());
            sMessage.setSender(message.getSender().getUserName());
            sMessage.setSenderID(message.getSender().getId());
            sMessage.setRecipient(message.getRecipient().getUserName());
            sMessage.setRecipientID(message.getRecipient().getId());
            sMessage.setTimeSend(message.getTimeSend());
            sMessage.setContent(message.getContent());

            sMessageList.add(sMessage);
        }

        return sMessageList;
    }
}
