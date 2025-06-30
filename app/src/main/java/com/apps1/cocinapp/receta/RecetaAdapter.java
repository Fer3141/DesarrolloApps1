package com.apps1.cocinapp.receta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.recetas.DetalleRecetaActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {

    private Context context;
    private List<RecetaResumenDTO> lista;

    public RecetaAdapter(Context context, List<RecetaResumenDTO> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.card_receta, parent, false);
        return new RecetaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder, int position) {
        RecetaResumenDTO receta = lista.get(position);

        holder.nombreReceta.setText(receta.nombreReceta);
        holder.nombreUsuario.setText(receta.nombreUsuario);
        holder.valoracionReceta.setRating((float) receta.promedio);

        // si la url es válida usamos picasso
        if (receta.fotoPrincipal != null && !receta.fotoPrincipal.isEmpty()) {
            Picasso.get()
                    .load(receta.fotoPrincipal)
                    .placeholder(R.drawable.pholder) // imagen por defecto
                    .into(holder.imagenReceta);
        } else {
            holder.imagenReceta.setImageResource(R.drawable.pholder);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleRecetaActivity.class);
            intent.putExtra("idReceta", receta.idReceta); // mandás el ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<RecetaResumenDTO> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }

    static class RecetaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenReceta;
        TextView nombreReceta, nombreUsuario;
        RatingBar valoracionReceta;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.imagenReceta);
            nombreReceta = itemView.findViewById(R.id.tituloReceta);
            nombreUsuario = itemView.findViewById(R.id.autorReceta);
            valoracionReceta = itemView.findViewById(R.id.ratingReceta);
        }
    }
}
