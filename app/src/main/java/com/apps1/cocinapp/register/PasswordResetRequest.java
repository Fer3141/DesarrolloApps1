package com.apps1.cocinapp.register;

public class PasswordResetRequest {
    private String mail;
    private String nuevaPassword;

    public PasswordResetRequest(String mail, String nuevaPassword) {
        this.mail = mail;
        this.nuevaPassword = nuevaPassword;
    }

    public String getMail() {
        return mail;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }
}

