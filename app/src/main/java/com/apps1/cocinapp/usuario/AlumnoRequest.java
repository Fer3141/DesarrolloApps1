package com.apps1.cocinapp.usuario;

public class AlumnoRequest {

    private Long idUsuario;
    private String nroTramiteDni;
    private String numeroTarjeta;


    public AlumnoRequest(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public AlumnoRequest(Long idUsuario, String nroTramiteDni, String numeroTarjeta) {
        this.idUsuario = idUsuario;
        this.nroTramiteDni = nroTramiteDni;
        this.numeroTarjeta = numeroTarjeta;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNroTramiteDni() {
        return nroTramiteDni;
    }

    public void setNroTramiteDni(String nroTramiteDni) {
        this.nroTramiteDni = nroTramiteDni;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }
}
