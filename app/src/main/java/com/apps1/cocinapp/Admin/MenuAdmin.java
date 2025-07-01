package com.apps1.cocinapp.Admin;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.apps1.cocinapp.session.SharedPreferencesHelper;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecetaAdminAdapter adapter;
    private Long idUsuario;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        recyclerView = findViewById(R.id.recyclerRecetasPendientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnCurso = findViewById(R.id.btnCrearCurso);
        Button btnSede = findViewById(R.id.btnCrearSede);
        Button btnCronograma = findViewById(R.id.btnCrearCronograma);

        idUsuario = SharedPreferencesHelper.obtenerIdUsuario(this);
        apiService = RetrofitClient.getInstance().getApi();

        cargarRecetasPendientes();

        btnCurso.setOnClickListener(v -> {
            Toast.makeText(this, "Crear Curso", Toast.LENGTH_SHORT).show();
        });

        btnSede.setOnClickListener(v -> {
            Toast.makeText(this, "Crear Sede", Toast.LENGTH_SHORT).show();
        });

        btnCronograma.setOnClickListener(v -> {
            Toast.makeText(this, "Crear Cronograma", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarRecetasPendientes() {
        apiService.getRecetasPendientes(idUsuario).enqueue(new Callback<List<RecetaDetalleDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaDetalleDTO>> call, Response<List<RecetaDetalleDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new RecetaAdminAdapter(response.body(), idUsuario);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MenuAdmin.this, "No se pudieron cargar las recetas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaDetalleDTO>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(MenuAdmin.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
