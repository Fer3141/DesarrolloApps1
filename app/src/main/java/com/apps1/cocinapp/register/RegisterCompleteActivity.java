package com.apps1.cocinapp.register;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

public class RegisterCompleteActivity extends AppCompatActivity {

    EditText inputNombre, inputApellido, inputFecha, inputGenero, inputPassword, inputConfirmarPassword;
    EditText inputTramiteDni;
    LinearLayout contenedorAlumno;
    Button btnDniFrente, btnDniDorso, btnAgregarPago, btnFinalizar;
    Button tabUsuario, tabAlumno;
    CheckBox termsCheckbox;

    boolean esAlumno = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        // Inputs generales
        inputNombre = findViewById(R.id.inputNombre);
        inputApellido = findViewById(R.id.inputApellido);
        inputFecha = findViewById(R.id.inputFecha);
        inputGenero = findViewById(R.id.inputGenero);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmarPassword = findViewById(R.id.inputConfirmarPassword);

        // Alumno
        contenedorAlumno = findViewById(R.id.contenedorAlumno);
        inputTramiteDni = findViewById(R.id.inputTramiteDni);
        btnDniFrente = findViewById(R.id.btnDniFrente);
        btnDniDorso = findViewById(R.id.btnDniDorso);
        btnAgregarPago = findViewById(R.id.btnAgregarPago);

        // Tabs
        tabUsuario = findViewById(R.id.tabUsuario);
        tabAlumno = findViewById(R.id.tabAlumno);

        // Otros
        termsCheckbox = findViewById(R.id.termsCheckbox);
        btnFinalizar = findViewById(R.id.finalizarBtn);

        // Por defecto: usuario
        esAlumno = false;
        actualizarTabs();

        tabUsuario.setOnClickListener(v -> {
            esAlumno = false;
            actualizarTabs();
        });

        tabAlumno.setOnClickListener(v -> {
            esAlumno = true;
            actualizarTabs();
        });

        btnFinalizar.setOnClickListener(v -> {
            String pass = inputPassword.getText().toString().trim();
            String confirm = inputConfirmarPassword.getText().toString().trim();

            if (pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Por favor ingresá la contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!termsCheckbox.isChecked()) {
                Toast.makeText(this, "Debés aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esAlumno) {
                String tramite = inputTramiteDni.getText().toString().trim();
                if (tramite.isEmpty()) {
                    Toast.makeText(this, "Falta el N° de trámite del DNI", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "Registro completado con éxito", Toast.LENGTH_LONG).show();
            finish(); // o redireccionar a login
        });
    }

    private void actualizarTabs() {
        if (esAlumno) {
            contenedorAlumno.setVisibility(View.VISIBLE);
            tabAlumno.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E68523")));
            tabAlumno.setTextColor(Color.WHITE);
            tabUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF5E9")));
            tabUsuario.setTextColor(Color.parseColor("#E68523"));
        } else {
            contenedorAlumno.setVisibility(View.GONE);
            tabUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E68523")));
            tabUsuario.setTextColor(Color.WHITE);
            tabAlumno.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF5E9")));
            tabAlumno.setTextColor(Color.parseColor("#E68523"));
        }
    }
}
