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
import com.apps1.cocinapp.session.SharedPreferencesHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
            // Aquí podrías recoger todos los datos y guardarlos o enviarlos
            // Por ahora solo cerrar o mostrar mensaje
            finish();
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
