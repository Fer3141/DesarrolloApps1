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
import com.apps1.cocinapp.dto.MotivoRechazoDTO;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetaPendienteAdapter extends RecyclerView.Adapter<RecetaPendienteAdapter.ViewHolder> {

    public interface OnRecetaAdminClickListener {
        void onAprobar(RecetaDetalleDTO receta);
        void onRechazar(RecetaDetalleDTO receta);

        void onClickDetalle(RecetaDetalleDTO receta);
    }

    private List<RecetaDetalleDTO> recetas;
    private Context context;
    private OnRecetaAdminClickListener listener;

    public RecetaPendienteAdapter(List<RecetaDetalleDTO> recetas, Context context, OnRecetaAdminClickListener listener) {
        this.recetas = recetas;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_receta_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecetaDetalleDTO receta = recetas.get(position);

        holder.nombre.setText(receta.nombreReceta);
        holder.descripcion.setText(receta.descripcionReceta);

        if (receta.fotoPrincipal != null && !receta.fotoPrincipal.isEmpty()) {
            Picasso.get().load(receta.fotoPrincipal).placeholder(R.drawable.pholder).into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.pholder);
        }

        holder.btnAprobar.setOnClickListener(v -> listener.onAprobar(receta));
        holder.btnRechazar.setOnClickListener(v -> listener.onRechazar(receta));

        holder.itemView.setOnClickListener(v -> {
            listener.onClickDetalle(receta);
        });

    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public void actualizarLista(List<RecetaDetalleDTO> nuevas) {
        this.recetas = nuevas;
        notifyDataSetChanged();
    }

    public void eliminarReceta(RecetaDetalleDTO receta) {
        int index = recetas.indexOf(receta);
        if (index >= 0) {
            recetas.remove(index);
            notifyItemRemoved(index);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion;
        ImageView foto;
        Button btnAprobar, btnRechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreAdmin);
            descripcion = itemView.findViewById(R.id.txtDescripcionAdmin);
            foto = itemView.findViewById(R.id.imgFotoAdmin);
            btnAprobar = itemView.findViewById(R.id.btnAprobar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
        }
    }
}

