package com.apps1.cocinapp.recetas;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.dto.RecetaResumenDTO;
import com.apps1.cocinapp.receta.RecetaAdapter;
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.apps1.cocinapp.usuario.PerfilActivity;
import com.apps1.cocinapp.usuario.PerfilDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisRecetasFavoritasActivity extends AppCompatActivity {

    RecyclerView recyclerFavoritas;
    private RecetaAdapter adapter;
    Long usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_recetas_favoritas);

        recyclerFavoritas = findViewById(R.id.recyclerFavoritas);


        recyclerFavoritas = findViewById(R.id.recyclerFavoritas);
        recyclerFavoritas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, new ArrayList<>());
        recyclerFavoritas.setAdapter(adapter);

        // acá luego podés setear el adapter con tus recetas favoritas
        ApiService api = RetrofitClient.getInstance().getApi();
        String token = SharedPreferencesHelper.obtenerToken(this);
        usuarioId = SharedPreferencesHelper.obtenerIdUsuario(this);

        Call<List<RecetaResumenDTO>> call = api.getFavoritos(usuarioId);
        call.enqueue(new Callback<List<RecetaResumenDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecetaResumenDTO> favoritos = response.body();
                    adapter.actualizarLista(response.body());
                } else {
                    Toast.makeText(MisRecetasFavoritasActivity.this, "Error al obtener favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                Toast.makeText(MisRecetasFavoritasActivity.this, "Fallo la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
