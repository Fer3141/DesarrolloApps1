package com.apps1.cocinapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.dto.CalificacionVistaDTO;

import java.util.List;

public class ComentarioPendienteAdapter extends RecyclerView.Adapter<ComentarioPendienteAdapter.ViewHolder> {

    private List<CalificacionVistaDTO> comentarios;
    private Context context;
    private OnComentarioClickListener listener;

    public ComentarioPendienteAdapter(List<CalificacionVistaDTO> comentarios, Context context, OnComentarioClickListener listener) {
        this.comentarios = comentarios;
        this.context = context;
        this.listener = listener;
    }

    public interface OnComentarioClickListener {
        void onAprobar(Long idComentario);
        void onRechazar(Long idComentario);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.card_comentario_pendiente, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalificacionVistaDTO comentario = comentarios.get(position);

        holder.tvNombreReceta.setText("Receta: " + comentario.getNombreReceta());
        holder.tvComentario.setText("Comentario: " + comentario.getComentario());

        holder.btnAprobar.setOnClickListener(v -> listener.onAprobar(comentario.getIdCalificacion()));
        holder.btnRechazar.setOnClickListener(v -> listener.onRechazar(comentario.getIdCalificacion()));
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreReceta, tvComentario;
        Button btnAprobar, btnRechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreReceta = itemView.findViewById(R.id.tvNombreReceta);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            btnAprobar = itemView.findViewById(R.id.btnAprobarComentario);
            btnRechazar = itemView.findViewById(R.id.btnRechazarComentario);
        }
    }
}
