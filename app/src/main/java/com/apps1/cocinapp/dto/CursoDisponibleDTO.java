package com.apps1.cocinapp.dto;

import java.util.List;

public class CursoDisponibleDTO {
    public Long idCronograma;
    public String descripcionCurso;
    public String direccion;
    public String fechaInicio; // formato: yyyy-MM-dd
    public String fechaFin;
    public double precioFinal;

    public List<CronogramaDTO> cronogramas;


}

