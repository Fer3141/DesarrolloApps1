package com.apps1.cocinapp.register;

public class LoginRequest {
    private String mail;
    private String password;

    public LoginRequest(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public String getMail() { return mail; }
    public String getPassword() { return password; }
}
