package com.apps1.cocinapp.register;

public class RegistroUsuarioRequest {
    private String email;
    private boolean alumno;
    private String nombre;
    private String apellido;
    private String password;
    private String fotoDniFrente;
    private String fotoDniDorso;
    private String nroTramiteDni;
    private String cuentaCorriente;

    public RegistroUsuarioRequest(String email, boolean alumno, String nombre, String apellido,
                                  String password, String fotoDniFrente, String fotoDniDorso,
                                  String nroTramiteDni, String cuentaCorriente) {
        this.email = email;
        this.alumno = alumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.fotoDniFrente = fotoDniFrente;
        this.fotoDniDorso = fotoDniDorso;
        this.nroTramiteDni = nroTramiteDni;
        this.cuentaCorriente = cuentaCorriente;
    }

    public String getEmail() { return email; }
    public boolean isAlumno() { return alumno; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getPassword() { return password; }
    public String getFotoDniFrente() { return fotoDniFrente; }
    public String getFotoDniDorso() { return fotoDniDorso; }
    public String getNroTramiteDni() { return nroTramiteDni; }
    public String getCuentaCorriente() { return cuentaCorriente; }
}
