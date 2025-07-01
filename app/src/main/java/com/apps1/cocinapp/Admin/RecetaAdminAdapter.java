package com.apps1.cocinapp.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.apps1.cocinapp.api.ApiService;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetaAdminAdapter extends RecyclerView.Adapter<RecetaAdminAdapter.RecetaAdminViewHolder> {

    private Context context;
    private List<RecetaDetalleDTO> lista;
    private ApiService apiService;
    private Long idAdmin;

    public RecetaAdminAdapter(List<RecetaDetalleDTO> lista, Long idAdmin) {
        this.context = context;
        this.lista = lista;
        this.idAdmin = idAdmin;
        this.apiService = RetrofitClient.getInstance().getApi();
    }

    @NonNull
    @Override
    public RecetaAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.card_receta_admin, parent, false);
        return new RecetaAdminViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaAdminViewHolder holder, int position) {
        RecetaDetalleDTO receta = lista.get(position);

        holder.nombreReceta.setText(receta.nombreReceta);
        holder.nombreUsuario.setText("Autor: " + receta.nombreUsuario);
        holder.descripcion.setText(receta.descripcionReceta);

        if (receta.fotoPrincipal != null && !receta.fotoPrincipal.isEmpty()) {
            Picasso.get()
                    .load(receta.fotoPrincipal)
                    .placeholder(R.drawable.pholder)
                    .into(holder.imagenReceta);
        } else {
            holder.imagenReceta.setImageResource(R.drawable.pholder);
        }

        holder.btnAprobar.setOnClickListener(v -> {
            apiService.aprobarReceta(receta.idReceta, idAdmin).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        lista.remove(pos);
                        notifyItemRemoved(pos);
                        Toast.makeText(context, "Receta aprobada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error al aprobar", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btnRechazar.setOnClickListener(v -> {
            EditText input = new EditText(context);
            input.setHint("Motivo del rechazo");
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            new AlertDialog.Builder(context)
                    .setTitle("Rechazar receta")
                    .setMessage("Escribí el motivo del rechazo:")
                    .setView(input)
                    .setPositiveButton("Rechazar", (dialog, which) -> {
                        String motivo = input.getText().toString().trim();
                        Map<String, String> body = new HashMap<>();
                        body.put("motivo", motivo);

                        apiService.rechazarReceta(receta.idReceta, idAdmin, body).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                int pos = holder.getAdapterPosition();
                                if (pos != RecyclerView.NO_POSITION) {
                                    lista.remove(pos);
                                    notifyItemRemoved(pos);
                                    Toast.makeText(context, "Receta rechazada", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Error al rechazar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<RecetaDetalleDTO> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }

    static class RecetaAdminViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenReceta;
        TextView nombreReceta, nombreUsuario, descripcion;
        Button btnAprobar, btnRechazar;

        public RecetaAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenReceta = itemView.findViewById(R.id.imagenRecetaAdmin);
            nombreReceta = itemView.findViewById(R.id.tituloRecetaAdmin);
            nombreUsuario = itemView.findViewById(R.id.autorRecetaAdmin);
            descripcion = itemView.findViewById(R.id.descripcionRecetaAdmin);
            btnAprobar = itemView.findViewById(R.id.btnAprobar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
        }
    }
}
