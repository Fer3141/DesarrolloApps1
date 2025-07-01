package com.apps1.cocinapp.dto;

public class RecetaPendienteDTO {
    private Long idReceta;
    private String nombreReceta;
    private String descripcionReceta;
    private String fotoPrincipal;
    private String autor;

    public Long getIdReceta() { return idReceta; }
    public void setIdReceta(Long idReceta) { this.idReceta = idReceta; }

    public String getNombreReceta() { return nombreReceta; }
    public void setNombreReceta(String nombreReceta) { this.nombreReceta = nombreReceta; }

    public String getDescripcionReceta() { return descripcionReceta; }
    public void setDescripcionReceta(String descripcionReceta) { this.descripcionReceta = descripcionReceta; }

    public String getFotoPrincipal() { return fotoPrincipal; }
    public void setFotoPrincipal(String fotoPrincipal) { this.fotoPrincipal = fotoPrincipal; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
}
