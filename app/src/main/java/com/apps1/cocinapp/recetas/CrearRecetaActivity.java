package com.apps1.cocinapp.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearRecetaActivity extends AppCompatActivity {

    EditText tituloInput, descripcionInput;
    Button agregarIngredienteBtn, agregarPasoBtn, publicarBtn;
    LinearLayout contenedorIngredientes, contenedorPasos;

    boolean estaLogueado;
    LinearLayout menuOpciones;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);

        // Referencias
        tituloInput = findViewById(R.id.nombreInput);
        descripcionInput = findViewById(R.id.descripcionInput);
        agregarIngredienteBtn = findViewById(R.id.btnAgregarIngrediente);
        agregarPasoBtn = findViewById(R.id.btnAgregarPaso);
        publicarBtn = findViewById(R.id.btnPublicar);
        contenedorIngredientes = findViewById(R.id.contenedorIngredientes);
        contenedorPasos = findViewById(R.id.contenedorPasos);
        estaLogueado = SharedPreferencesHelper.hayToken(this);
        menuOpciones = findViewById(R.id.menuOpciones);


        LayoutInflater inflater = LayoutInflater.from(this);

        // Agregar nuevo ingrediente
        agregarIngredienteBtn.setOnClickListener(v -> {
            View itemIngrediente = inflater.inflate(R.layout.item_ingrediente, contenedorIngredientes, false);
            contenedorIngredientes.addView(itemIngrediente);
        });

        // Agregar nuevo paso
        agregarPasoBtn.setOnClickListener(v -> {
            View item = getLayoutInflater().inflate(R.layout.item_paso, contenedorPasos, false);
            contenedorPasos.addView(item);
        });

        // Acción de publicación (a completar con lógica real)
        publicarBtn.setOnClickListener(v -> {

            String titulo = tituloInput.getText().toString().trim();
            if (titulo.isEmpty()) {
                Toast.makeText(CrearRecetaActivity.this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Toast.makeText(CrearRecetaActivity.this, titulo, Toast.LENGTH_SHORT).show();
            }
            String descripcion = descripcionInput.getText().toString().trim();

            List<IngredienteDTO> ingredientes = new ArrayList<>();
            for (int i = 0; i < contenedorIngredientes.getChildCount(); i++) {
                View view = contenedorIngredientes.getChildAt(i);
                EditText nombreInput = view.findViewById(R.id.nombreIngredienteInput);
                EditText cantidadInput = view.findViewById(R.id.cantidadIngredienteInput);

                String nombre = nombreInput.getText().toString().trim();
                String cantidadStr = cantidadInput.getText().toString().trim();

                if (!nombre.isEmpty() && !cantidadStr.isEmpty()) {
                    try {
                        float cantidad = Float.parseFloat(cantidadStr);
                        ingredientes.add(new IngredienteDTO(nombre, cantidad));
                    } catch (NumberFormatException ignored) {}
                }
            }

            List<PasoDTO> pasos = new ArrayList<>();
            for (int i = 0; i < contenedorPasos.getChildCount(); i++) {
                View view = contenedorPasos.getChildAt(i);
                EditText tituloPaso = view.findViewById(R.id.tituloPasoInput);
                EditText descripcionPaso = view.findViewById(R.id.descripcionPasoInput);

                String tituloP = tituloPaso.getText().toString().trim();
                String descP = descripcionPaso.getText().toString().trim();

                if (!tituloP.isEmpty() && !descP.isEmpty()) {
                    pasos.add(new PasoDTO(tituloP, descP));
                }
            }

            RecetaDTO recetaDTO = new RecetaDTO(pasos, ingredientes, descripcion, titulo);

            // Enviar al backend
            ApiService api = RetrofitClient.getInstance().getApi();
            String token = SharedPreferencesHelper.obtenerToken(this);
            Call<Void> call = api.crearReceta("Bearer " + token, recetaDTO);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CrearRecetaActivity.this, "Receta publicada", Toast.LENGTH_SHORT).show();
                        finish(); // volver
                    } else {
                        Toast.makeText(CrearRecetaActivity.this, "Error al publicar receta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CrearRecetaActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });



        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Siempre va al inicio
                Intent intent = new Intent(CrearRecetaActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.nav_menu) {
                // Si está logueado, va a opciones
                if (estaLogueado) {
                    Toast.makeText(this, "Ir a opciones", Toast.LENGTH_SHORT).show();
                    if (menuOpciones.getVisibility() == View.GONE) {
                        menuOpciones.setVisibility(View.VISIBLE);
                    } else {
                        menuOpciones.setVisibility(View.GONE);
                    }
                }
                return true;

            } else if (itemId == R.id.nav_perfil) {
                if (!estaLogueado) {
                    Intent intent = new Intent(CrearRecetaActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Ir a perfil", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(this, PerfilActivity.class));
                }
                return true;
            }

            return false;
        });
    }
}
