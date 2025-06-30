package com.apps1.cocinapp.recetas;

import com.apps1.cocinapp.receta.RecetaAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisRecetasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecetaAdapter adapter;
    private List<RecetaResumenDTO> misRecetas = new ArrayList<>();
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.recetasRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, misRecetas);
        recyclerView.setAdapter(adapter);

        usuarioId = SharedPreferencesHelper.obtenerIdUsuario(this).intValue();

        if (!hayInternet()) {
            Toast.makeText(this, "Sin conexión a internet", Toast.LENGTH_SHORT).show();
            return;
        }

        cargarMisRecetas();
    }

    private void cargarMisRecetas() {
        //Obtener todas las recetas del usuario
        RetrofitClient.getInstance().getApi().getMisRecetas(usuarioId).enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    misRecetas = response.body();
                    adapter.actualizarLista(misRecetas);
                } else {
                    Toast.makeText(MisRecetasActivity.this, "No se pudieron cargar tus recetas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(MisRecetasActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean hayInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}

