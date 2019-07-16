package com.example.YakServer.Responds;


public class AuthResponse {
    private String Response;
    private String Id;

    public AuthResponse() {

    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}
