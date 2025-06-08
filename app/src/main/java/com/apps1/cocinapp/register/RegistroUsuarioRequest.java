
package com.apps1.cocinapp.register;

public class RegistroUsuarioRequest {
    private String mail;
    private String alias;
    private String password;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String genero;
    private boolean habilitado;
    private String tipo;

    // campos adicionales para alumnos
    private String tramiteDni;
    private String fotoDniFrente;
    private String fotoDniDorso;
    private String medioPago;
    private String direccion;
    private String cuentaCorriente;

    // Getters y Setters

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTramiteDni() { return tramiteDni; }
    public void setTramiteDni(String tramiteDni) { this.tramiteDni = tramiteDni; }

    public String getFotoDniFrente() { return fotoDniFrente; }
    public void setFotoDniFrente(String fotoDniFrente) { this.fotoDniFrente = fotoDniFrente; }

    public String getFotoDniDorso() { return fotoDniDorso; }
    public void setFotoDniDorso(String fotoDniDorso) { this.fotoDniDorso = fotoDniDorso; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCuentaCorriente() { return cuentaCorriente; }
    public void setCuentaCorriente(String cuentaCorriente) { this.cuentaCorriente = cuentaCorriente; }
}
