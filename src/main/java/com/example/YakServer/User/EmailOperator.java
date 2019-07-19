package com.example.YakServer.User;

import com.example.YakServer.Models.User;

import com.example.YakServer.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.JsonNode;

import java.util.*;

@Service
class EmailOperator {
    private UserRepository userRepository;

    @Autowired
    EmailOperator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    boolean activate(Integer code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) return false;
        else {
            user.setActivate(true);
            user.setActivationCodeSend(true);
            return true;
        }
    }

    Integer generateCode() {
        Random rnd = new Random();
        return 1000000 + rnd.nextInt(9000000);
    }

    void sendSimpleMessage() throws UnirestException {
        final String YOUR_DOMAIN_NAME = "https://yak-server.herokuapp.com/";
        final String API_KEY = "983d5e3168b20f60e8c9d7c068785341-fd0269a6-d7b24600";

        HttpResponse<String> request = Unirest.post("https://api.eu.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
                .basicAuth("api", API_KEY)
                .field("from", "Excited User <mxa.mailgun.org>")
                .field("to", "kordian_niemczyk@o2.com")
                .field("subject", "hello")
                .field("text", "testing")
                .asString();

        System.out.println(request.getBody());

//        return request.getBody();
    }
}

