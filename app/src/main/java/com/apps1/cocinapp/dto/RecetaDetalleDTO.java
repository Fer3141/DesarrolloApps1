
package com.apps1.cocinapp.dto;

import java.util.List;

public class RecetaDetalleDTO {
    public Long idReceta;
    public String nombreReceta;
    public String descripcionReceta;
    public String fotoPrincipal;
    public int porciones;
    public int cantidadPersonas;
    public int idTipo;
    public String nombreUsuario;
    public List<IngredienteDTO> ingredientes;
    public List<PasoCompletoDTO> pasos;
}
