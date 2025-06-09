package com.apps1.cocinapp.register;

public class RegistroRequest {
    private String email;
    private String alias;

    public RegistroRequest(String email, String alias) {
        this.email = email;
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public String getAlias() {
        return alias;
    }
}
