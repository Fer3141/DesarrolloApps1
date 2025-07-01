package com.apps1.cocinapp.dto;

public class RecetaResumenDTO {
    public Long idReceta;
    public String nombreReceta;
    public String fotoPrincipal;
    public String nombreUsuario;
    public double promedio;
    public String estado;
    public RecetaResumenDTO(Long idReceta, String nombreReceta, String fotoPrincipal,
                            String nombreUsuario, double promedio) {
        this(idReceta, nombreReceta, fotoPrincipal, nombreUsuario, promedio, null);
    }

    // contructor para mis recetas
    public RecetaResumenDTO(Long id, String nombre, String foto, String usuario, double promedio, String estado) {
        this.idReceta = id;
        this.nombreReceta = nombre;
        this.fotoPrincipal = foto;
        this.nombreUsuario = usuario;
        this.promedio = promedio;
        this.estado = estado;
    }

    public Long getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Long idReceta) {
        this.idReceta = idReceta;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public void setFotoPrincipal(String fotoPrincipal) {
        this.fotoPrincipal = fotoPrincipal;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
}
