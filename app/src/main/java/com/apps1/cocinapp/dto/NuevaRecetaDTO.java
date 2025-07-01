package com.apps1.cocinapp.dto;

import java.util.List;

public class NuevaRecetaDTO {
    public String nombreReceta;
    public String descripcionReceta;
    public String fotoPrincipal;
    public int porciones;
    public int cantidadPersonas;

    public Long idUsuario; // id del user, se saca del token
    public List<IngredienteDTO> ingredientes;
    public List<PasoCompletoDTO> pasos;
    public String tipo;
}