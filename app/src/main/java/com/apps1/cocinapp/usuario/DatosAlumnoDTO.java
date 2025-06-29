package com.apps1.cocinapp.usuario;

public class DatosAlumnoDTO {
    public String fotoDniFrente;
    public String fotoDniDorso;
    public String nroTramiteDni;
    public String numeroTarjeta;
    public String cuentaCorriente;

    public DatosAlumnoDTO(String frente, String dorso, String tramite, String tarjeta, String cuenta) {
        this.fotoDniFrente = frente;
        this.fotoDniDorso = dorso;
        this.nroTramiteDni = tramite;
        this.numeroTarjeta = tarjeta;
        this.cuentaCorriente = cuenta;
    }
}

