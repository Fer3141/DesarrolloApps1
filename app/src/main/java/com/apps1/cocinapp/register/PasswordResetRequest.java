package com.apps1.cocinapp.register;

public class PasswordResetRequest {
    private String mail;
    private String pass;
    private String codigo;

    public PasswordResetRequest(String mail, String pass, String codigo) {
        this.mail = mail;
        this.pass = pass;
        this.codigo = codigo;
    }

    public String getMail() { return mail; }
    public String getPass() { return pass; }
    public String getCodigo() { return codigo; }
}
