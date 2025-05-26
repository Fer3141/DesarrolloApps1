package com.apps1.cocinapp.data;

public class Receta {
    public String titulo;
    public String autor;
    public int imagenResId;
    public float rating;

    public Receta(String titulo, String autor, int imagenResId, float rating) {
        this.titulo = titulo;
        this.autor = autor;
        this.imagenResId = imagenResId;
        this.rating = rating;
    }
}
