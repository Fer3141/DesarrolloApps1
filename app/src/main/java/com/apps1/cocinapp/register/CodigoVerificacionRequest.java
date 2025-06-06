package com.apps1.cocinapp.register;

public class CodigoVerificacionRequest {
    private String mail;
    private String codigo;

    public CodigoVerificacionRequest(String mail, String codigo) {
        this.mail = mail;
        this.codigo = codigo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

