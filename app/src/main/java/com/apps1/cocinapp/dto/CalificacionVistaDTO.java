package com.apps1.cocinapp.dto;

public class CalificacionVistaDTO {
    private String aliasUsuario;
    private int calificacion;
    private String comentario;
    private Boolean aprobado;
    private String nombreReceta;
    private Long idCalificacion;

    // Constructor para uso público (usuario común)
    public CalificacionVistaDTO(String aliasUsuario, int calificacion, String comentario, Boolean aprobado) {
        this.aliasUsuario = aliasUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.aprobado = aprobado;
    }

    // Constructor para uso del admin
    public CalificacionVistaDTO(String aliasUsuario, int calificacion, String comentario, Boolean aprobado,
                                String nombreReceta, Long idCalificacion) {
        this.aliasUsuario = aliasUsuario;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.aprobado = aprobado;
        this.nombreReceta = nombreReceta;
        this.idCalificacion = idCalificacion;
    }

    // Getters y setters

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public void setAliasUsuario(String aliasUsuario) {
        this.aliasUsuario = aliasUsuario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public Long getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Long idCalificacion) {
        this.idCalificacion = idCalificacion;
    }
}
