package com.apps1.cocinapp.receta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.data.Receta;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {
    private List<Receta> recetas;

    public RecetaAdapter(List<Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public RecetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_receta, parent, false);
        return new RecetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecetaViewHolder holder, int position) {
        Receta r = recetas.get(position);
        holder.titulo.setText(r.titulo);
        holder.autor.setText(r.autor);
        holder.imagen.setImageResource(r.imagenResId);
        holder.rating.setRating(r.rating);
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo, autor;
        RatingBar rating;

        public RecetaViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenReceta);
            titulo = itemView.findViewById(R.id.tituloReceta);
            autor = itemView.findViewById(R.id.autorReceta);
            rating = itemView.findViewById(R.id.ratingReceta);
        }
    }
}
