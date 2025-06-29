package com.apps1.cocinapp.recetas;

public class IngredienteDTO {
    private String nombre;
    private float cantidad;

    public IngredienteDTO() {
    }

    public IngredienteDTO(String nombre, float cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }
}
