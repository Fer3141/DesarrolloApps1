package com.apps1.cocinapp.recetas;

import com.apps1.cocinapp.receta.MisRecetasAdapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private MisRecetasAdapter adapter;
    private List<RecetaResumenDTO> misRecetas = new ArrayList<>();
    private TextView mensajeSinConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.setBackgroundColor(Color.WHITE);

        mensajeSinConexion = new TextView(this);
        mensajeSinConexion.setText("Sin conexión a internet");
        mensajeSinConexion.setTextSize(18);
        mensajeSinConexion.setTextColor(Color.RED);
        mensajeSinConexion.setVisibility(TextView.GONE);

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        layout.addView(mensajeSinConexion);
        layout.addView(recyclerView);

        setContentView(layout);

        adapter = new MisRecetasAdapter(this, misRecetas);
        recyclerView.setAdapter(adapter);

        if (!hayInternet()) {
            mensajeSinConexion.setVisibility(TextView.VISIBLE);
            recyclerView.setVisibility(RecyclerView.GONE);
        } else {
            mensajeSinConexion.setVisibility(TextView.GONE);
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            cargarMisRecetas();
        }
    }

    private void cargarMisRecetas() {
        Long userId = SharedPreferencesHelper.obtenerIdUsuario(this);
        if (userId == null) {
            Toast.makeText(this, "No se encontró usuario logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getInstance()
                .getApi()
                .getRecetasPorUsuario(userId)
                .enqueue(new Callback<List<RecetaResumenDTO>>() {
                    @Override
                    public void onResponse(Call<List<RecetaResumenDTO>> call, Response<List<RecetaResumenDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            misRecetas.clear();
                            misRecetas.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MisRecetasActivity.this, "No se pudieron cargar tus recetas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecetaResumenDTO>> call, Throwable t) {
                        Toast.makeText(MisRecetasActivity.this, "Error de red al cargar recetas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean hayInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hayInternet()) {
            cargarMisRecetas();
        }
    }
}
