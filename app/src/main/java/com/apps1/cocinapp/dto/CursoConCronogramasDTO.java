package com.apps1.cocinapp.dto;

import java.util.List;

public class CursoConCronogramasDTO {
    public Long idCurso;
    public String descripcion;
    public String contenidos;
    public String requerimientos;
    public int duracion;
    public double precio;
    public String modalidad;
    public List<CronogramaDTO> cronogramas;
}
