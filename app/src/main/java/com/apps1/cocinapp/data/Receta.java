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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
