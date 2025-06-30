package com.apps1.cocinapp.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificarNombreRecetaActivity extends AppCompatActivity {

    private EditText nombreInput;
    private Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);

        nombreInput = new EditText(this);
        nombreInput.setHint("Nombre del plato");

        btnSiguiente = new Button(this);
        btnSiguiente.setText("Siguiente");

        layout.addView(nombreInput);
        layout.addView(btnSiguiente);

        setContentView(layout);

        btnSiguiente.setOnClickListener(v -> verificarNombreReceta());
    }

    private void verificarNombreReceta() {
        String nombre = nombreInput.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingresá el nombre del plato", Toast.LENGTH_SHORT).show();
            return;
        }

        Long userIdLong = SharedPreferencesHelper.obtenerIdUsuario(this);
        if (userIdLong == null) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        int usuarioId = userIdLong.intValue();

        // llamo a tu API
        ApiService api = RetrofitClient.getInstance().getApi();
        //LLamar al endpoint del backend
        //Call<Boolean> call = api.existeRecetaPorUsuarioYNombre(usuarioId, nombre);
        // este endpoint debe devolver true/false

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean existe = response.body();
                    if (existe) {
                        mostrarDialog(nombre, usuarioId);
                    } else {
                        abrirCrearReceta("nuevo", nombre, usuarioId);
                    }
                } else {
                    Toast.makeText(VerificarNombreRecetaActivity.this, "Error consultando nombre", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(VerificarNombreRecetaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialog(String nombre, int usuarioId) {
        new AlertDialog.Builder(this)
                .setTitle("Receta existente")
                .setMessage("Ya cargaste una receta con este nombre. ¿Qué querés hacer?")
                .setPositiveButton("Reemplazar", (dialog, which) -> abrirCrearReceta("reemplazar", nombre, usuarioId))
                .setNeutralButton("Editar", (dialog, which) -> abrirCrearReceta("editar", nombre, usuarioId))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void abrirCrearReceta(String modo, String nombre, int usuarioId) {
        Intent intent = new Intent(this, CrearRecetaActivity.class);
        intent.putExtra("modo", modo);
        intent.putExtra("nombreReceta", nombre);
        intent.putExtra("usuarioId", usuarioId);
        startActivity(intent);
    }
}

