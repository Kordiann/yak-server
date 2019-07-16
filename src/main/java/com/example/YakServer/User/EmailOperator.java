package com.example.YakServer.User;

import com.example.YakServer.Models.User;
import com.example.YakServer.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

        if(user == null) return false;
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

    void sendEmail(String email) {
        try {
            // set params

            String k = "kordian_niemczyk@o2.pl";
//            TODO
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("smtp_account", "1.kordiann.smtp");
            params.put("subject", "Test java");
            params.put("html", "<p>Some Html</p>");
            params.put("text", "Test java");
            params.put("from", "cordixd@o2.pl");
            params.put("to", k);

            // setup connection
            URL url = new URL("https://api.emaillabs.net.pl/api/new_sendmail");

            // send data
            OutputStreamWriter out = new OutputStreamWriter(setupConnection(url).getOutputStream());
            out.write(buildQuery(params).toString());
            out.close();

            // read output
            BufferedReader in = new BufferedReader(new InputStreamReader(setupConnection(url).getInputStream()));
            System.out.print(in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setTemplateEmail(String email) {
        try {
            // set params
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("html",
                    "<html><head></head><body><h1>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</h1></body></html>");

            // setup connection
            URL url = new URL("https://api.emaillabs.net.pl/api/add_template");

            // send data
            OutputStreamWriter out = new OutputStreamWriter(setupConnection(url).getOutputStream());
            out.write(buildQuery(params).toString());
            out.close();

            // read output
            BufferedReader in = new BufferedReader(new InputStreamReader(setupConnection(url).getInputStream()));
            System.out.print(in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder buildQuery(HashMap<String, String> params) throws IOException {
            StringBuilder query = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    query.append("&");
                query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                query.append("=");
                query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        return query;
    }

    private HttpURLConnection setupConnection(URL url) throws IOException {
        String appKey = "d602122e10cb7410e3181356b21868a208fc2c15";
        String secretKey = "7a7f0ebdc50f02dec954bb72c4a2d2e4bc662ae8";
        String userpass = appKey + ":" + secretKey;
        String basicAuth = "Basic "
                + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", basicAuth);
        connection.setDoOutput(true);

        return connection;
    }
}

