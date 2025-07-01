package com.apps1.cocinapp.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.Admin.MenuAdmin;
import com.apps1.cocinapp.R;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.apps1.cocinapp.usuario.PerfilActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificarNombreRecetaActivity extends AppCompatActivity {

    private EditText nombreInput;
    private Button btnSiguiente;

    private boolean estaLogueado;
    BottomNavigationView bottomNav;

   // LinearLayout menuOpciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_verificar_nombre_receta);
        estaLogueado = false;
        bottomNav = findViewById(R.id.bottomNavigation);
        nombreInput = findViewById(R.id.nombreInput);
        btnSiguiente = findViewById(R.id.btnSiguiente);
     //   menuOpciones = findViewById(R.id.menuOpciones);

        String token = SharedPreferencesHelper.obtenerToken(this);

        if (token != null && !token.isEmpty()) {
            estaLogueado = true;
        }

        btnSiguiente.setOnClickListener(v -> verificarNombreReceta());

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_menu) {
                if (estaLogueado) {
                   // menuOpciones.setVisibility(menuOpciones.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                }
                return true;
            } else if (itemId == R.id.nav_perfil) {
                if (!estaLogueado) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    String rol = SharedPreferencesHelper.obtenerRolDelToken(this);
                    Toast.makeText(this, "Rol:" + rol, Toast.LENGTH_SHORT).show();

                    if (rol.equals("ADMIN")) {
                        startActivity(new Intent(this, MenuAdmin.class));
                    } else if (rol.equals("ALUMNO")) {
                        startActivity(new Intent(this, PerfilActivity.class));
                    } else {
                        // si no hay rol definido, redirigir al perfil por defecto
                        startActivity(new Intent(this, PerfilActivity.class));
                    }
                    startActivity(new Intent(this, PerfilActivity.class));
                }
                return true;
            }
            return false;
        });

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
        Long userId = SharedPreferencesHelper.obtenerIdUsuario(this);

        ApiService api = RetrofitClient.getInstance().getApi();
        //LLamar al endpoint del backend
        Call<Map<String, Object>> call = api.verificarNombreReceta(userId, nombre);
        // este endpoint debe devolver true/false

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();
                    Boolean existe = (Boolean) data.get("existe");
                    if (Boolean.TRUE.equals(existe)) {
                        mostrarDialog(nombre, userId);
                    } else {
                        abrirCrearReceta("nuevo", nombre, userId);
                    }
                } else {
                    Toast.makeText(VerificarNombreRecetaActivity.this, "Error consultando nombre", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(VerificarNombreRecetaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialog(String nombre, Long usuarioId) {
        new AlertDialog.Builder(this)
                .setTitle("Receta existente")
                .setMessage("Ya cargaste una receta con este nombre. ¿Qué querés hacer?")
                .setPositiveButton("Editar receta existente", (dialog, which) -> abrirCrearReceta("reemplazar", nombre, usuarioId))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void abrirCrearReceta(String modo, String nombre, Long usuarioId) {
        Intent intent = new Intent(this, CrearRecetaActivity.class);
        intent.putExtra("modo", modo);
        intent.putExtra("nombreReceta", nombre);
        intent.putExtra("usuarioId", usuarioId);
        startActivity(intent);
    }
}

