package com.apps1.cocinapp.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.dto.CronogramaDTO;
import com.apps1.cocinapp.dto.CursoConCronogramasDTO;

import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private List<CursoConCronogramasDTO> listaCursos;

    public CursoAdapter(List<CursoConCronogramasDTO> listaCursos) {
        this.listaCursos = listaCursos;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_curso, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        CursoConCronogramasDTO curso = listaCursos.get(position);

        holder.tvDescripcion.setText(curso.descripcion);
        holder.tvModalidad.setText("Modalidad: " + curso.modalidad);
        holder.tvPrecio.setText("Precio: $" + curso.precio);

        StringBuilder sb = new StringBuilder();
        if (curso.cronogramas == null || curso.cronogramas.isEmpty()) {
            sb.append("Sin cronogramas cargados.");
        } else {
            for (CronogramaDTO c : curso.cronogramas) {
                sb.append("- En sede ").append(c.sede)
                        .append("  Desde ").append(c.fechaInicio).append(" hasta ").append(c.fechaFin)
                        .append(" | Vacantes: ").append(c.vacantes).append("\n\n");
            }
        }

        holder.tvCronogramas.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvModalidad, tvPrecio, tvCronogramas;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionCurso);
            tvModalidad = itemView.findViewById(R.id.tvModalidadCurso);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCurso);
            tvCronogramas = itemView.findViewById(R.id.tvListaCronogramas);
        }
    }
}

