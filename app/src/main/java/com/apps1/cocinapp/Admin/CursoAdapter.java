package com.apps1.cocinapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.dto.CronogramaDTO;
import com.apps1.cocinapp.dto.CursoConCronogramasDTO;
import com.apps1.cocinapp.dto.CursoDisponibleDTO;

import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.ViewHolder> {

    private final List<CursoDisponibleDTO> cursos;
    private final Context context;

    public CursoAdapter(Context context, List<CursoDisponibleDTO> cursos) {
        this.context = context;
        this.cursos = cursos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_curso, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CursoDisponibleDTO curso = cursos.get(position);
        holder.descripcion.setText(curso.descripcionCurso);
        holder.sede.setText("Sede: " + curso.sede);
        holder.fechas.setText("Del " + curso.fechaInicio + " al " + curso.fechaFin);
        holder.precio.setText("Precio: $" + Math.abs(curso.precioFinal)); // si viene negativo
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion, sede, fechas, precio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion = itemView.findViewById(R.id.cursoDescripcion);
            sede = itemView.findViewById(R.id.cursoSede);
            fechas = itemView.findViewById(R.id.cursoFechas);
            precio = itemView.findViewById(R.id.cursoPrecio);
        }
    }
}
