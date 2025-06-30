package com.apps1.cocinapp.recetas;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class RecetaDTO {

    private long idUsuario;
    private String titulo;
    private String descripcion;
    private List<IngredienteDTO> ingredientes;
    private List<PasoDTO> pasos;

    public RecetaDTO() {
    }

    public RecetaDTO(List<PasoDTO> pasos, List<IngredienteDTO> ingredientes, String descripcion, String titulo) {
        this.pasos = pasos;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.titulo = titulo;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<IngredienteDTO> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteDTO> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<PasoDTO> getPasos() {
        return pasos;
    }

    public void setPasos(List<PasoDTO> pasos) {
        this.pasos = pasos;
    }
}
