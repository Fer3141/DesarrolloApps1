
package com.apps1.cocinapp.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.dto.MotivoRechazoDTO;
import com.apps1.cocinapp.dto.RecetaDetalleDTO;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.recetas.DetalleRecetaActivity;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdmin extends AppCompatActivity {

    private RecyclerView recycler;
    private RecetaPendienteAdapter adapter;
    private Long idAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        recycler = findViewById(R.id.recyclerRecetasPendientes);

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(v -> {
            SharedPreferencesHelper.eliminarToken(MenuAdmin.this);
            Intent intent = new Intent(MenuAdmin.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));

        idAdmin = SharedPreferencesHelper.obtenerIdUsuario(this);

        cargarRecetasPendientes();
    }

    private void cargarRecetasPendientes() {
        ApiService api = RetrofitClient.getInstance().getApi();
        api.getRecetasPendientes(idAdmin).enqueue(new Callback<List<RecetaDetalleDTO>>() {
            @Override
            public void onResponse(Call<List<RecetaDetalleDTO>> call, Response<List<RecetaDetalleDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RecetaDetalleDTO> lista = response.body();
                    adapter = new RecetaPendienteAdapter(lista, MenuAdmin.this, new RecetaPendienteAdapter.OnRecetaAdminClickListener() {
                        @Override
                        public void onAprobar(RecetaDetalleDTO receta) {
                            aprobarReceta(receta);
                        }

                        @Override
                        public void onRechazar(RecetaDetalleDTO receta) {
                            mostrarDialogoRechazo(receta);
                        }

                        @Override
                        public void onClickDetalle(RecetaDetalleDTO receta) {
                            Intent intent = new Intent(MenuAdmin.this, DetalleRecetaActivity.class);
                            intent.putExtra("idReceta", receta.getIdReceta());
                            startActivity(intent);
                        }
                    });
                    recycler.setAdapter(adapter);
                } else {
                    Toast.makeText(MenuAdmin.this, "Error al cargar recetas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecetaDetalleDTO>> call, Throwable t) {
                Toast.makeText(MenuAdmin.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aprobarReceta(RecetaDetalleDTO receta) {
        RetrofitClient.getInstance().getApi().aprobarReceta(receta.idReceta, idAdmin).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.eliminarReceta(receta);
                    Toast.makeText(MenuAdmin.this, "Receta aprobada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuAdmin.this, "Error al aprobar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MenuAdmin.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoRechazo(RecetaDetalleDTO receta) {
        EditText input = new EditText(this);
        input.setHint("Motivo del rechazo");

        new AlertDialog.Builder(this)
                .setTitle("Rechazar receta")
                .setView(input)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    String motivo = input.getText().toString().trim();
                    if (!motivo.isEmpty()) {
                        rechazarReceta(receta, motivo);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void rechazarReceta(RecetaDetalleDTO receta, String motivo) {
        Map<String, String> motivoMap = new HashMap<>();
        motivoMap.put("motivo", motivo);

        RetrofitClient.getInstance().getApi().rechazarReceta(receta.idReceta, idAdmin, motivoMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.eliminarReceta(receta);
                    Toast.makeText(MenuAdmin.this, "Receta rechazada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuAdmin.this, "Error al rechazar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MenuAdmin.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
