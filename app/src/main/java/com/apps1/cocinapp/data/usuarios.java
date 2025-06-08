package com.apps1.cocinapp.data;

public class usuarios {
    private int idUsuario;
    private String email;
    private String alias;
    private boolean habilitado;
    private String nombre;
    private String apellido;
    private String tipo;

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTipo() {
        return tipo;
    }

    // Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
