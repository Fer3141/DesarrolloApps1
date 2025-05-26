package com.apps1.cocinapp.receta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.data.Receta;
import com.apps1.cocinapp.recetas.DetalleRecetaActivity;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {
    private List<Receta> recetas;
    private Context context;

    public RecetaAdapter(Context context, List<Receta> recetas) {
        this.context = context;
        this.recetas = recetas;
    }

    @Override
    public RecetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_receta, parent, false);
        return new RecetaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecetaViewHolder holder, int position) {
        Receta r = recetas.get(position);
        holder.titulo.setText(r.getTitulo());
        holder.autor.setText(r.getAutor());
        holder.imagen.setImageResource(r.getImagenResId());
        holder.rating.setRating(r.getRating());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleRecetaActivity.class);
            intent.putExtra("titulo", r.getTitulo());
            intent.putExtra("autor", r.getAutor());
            intent.putExtra("rating", r.getRating());
            intent.putExtra("imagen", r.getImagenResId());
            context.startActivity(intent);
        });
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
