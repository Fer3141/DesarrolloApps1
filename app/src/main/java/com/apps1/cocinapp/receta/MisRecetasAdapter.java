package com.apps1.cocinapp.receta;

import com.apps1.cocinapp.recetas.VerificarNombreRecetaActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisRecetasAdapter extends RecyclerView.Adapter<MisRecetasAdapter.ViewHolder> {

    private Context context;
    private List<RecetaResumenDTO> recetas;

    public MisRecetasAdapter(Context context, List<RecetaResumenDTO> recetas) {
        this.context = context;
        this.recetas = recetas != null ? recetas : new java.util.ArrayList<>();
    }

    @NonNull
    @Override
    public MisRecetasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setBackgroundColor(Color.LTGRAY);

        TextView titulo = new TextView(context);
        titulo.setTextSize(20);
        layout.addView(titulo);

        TextView detalles = new TextView(context);
        detalles.setTextSize(16);
        layout.addView(detalles);

        Button btnEditar = new Button(context);
        btnEditar.setText("Editar");
        layout.addView(btnEditar);

        Button btnBorrar = new Button(context);
        btnBorrar.setText("Borrar");
        layout.addView(btnBorrar);

        return new ViewHolder(layout, titulo, detalles, btnEditar, btnBorrar);
    }

    @Override
    public void onBindViewHolder(@NonNull MisRecetasAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecetaResumenDTO receta = recetas.get(position);

        holder.titulo.setText(receta.getNombreReceta());
        holder.detalles.setText("Autor: " + receta.getNombreUsuario() +
                " | Rating: " + receta.getPromedio());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, VerificarNombreRecetaActivity.class);
            intent.putExtra("modo", "editar");
            intent.putExtra("idReceta", receta.getIdReceta());
            context.startActivity(intent);
        });

        holder.btnBorrar.setOnClickListener(v -> {
            Long userId = SharedPreferencesHelper.obtenerIdUsuario(context);
            if (userId == null) return;

            RetrofitClient.getInstance()
                    .getApi()
                    .eliminarReceta(userId, receta.getIdReceta())
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                recetas.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, recetas.size());
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {}
                    });
        });
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, detalles;
        Button btnEditar, btnBorrar;

        public ViewHolder(@NonNull LinearLayout itemView, TextView titulo, TextView detalles, Button btnEditar, Button btnBorrar) {
            super(itemView);
            this.titulo = titulo;
            this.detalles = detalles;
            this.btnEditar = btnEditar;
            this.btnBorrar = btnBorrar;
        }
    }
}