package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.login.LoginActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

public class RegisterCompleteActivity extends AppCompatActivity {

    EditText nombreInput, apellidoInput, passwordInput, confirmarPasswordInput, tramiteInput;
    Button finalizarBtn, btnDniFrente, btnDniDorso;
    Button tabUsuario, tabAlumno;
    LinearLayout contenedorAlumno;
    CheckBox termsCheckbox;
    boolean esAlumno = false;
    String email, alias;
    EditText fechaInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        tabUsuario = findViewById(R.id.tabUsuario);
        tabAlumno = findViewById(R.id.tabAlumno);
        contenedorAlumno = findViewById(R.id.contenedorAlumno);
        fechaInput = findViewById(R.id.inputFecha);
        nombreInput = findViewById(R.id.inputNombre);
        apellidoInput = findViewById(R.id.inputApellido);
        passwordInput = findViewById(R.id.inputPassword);
        confirmarPasswordInput = findViewById(R.id.inputConfirmarPassword);
        tramiteInput = findViewById(R.id.inputTramiteDni);
        btnDniFrente = findViewById(R.id.btnDniFrente);
        btnDniDorso = findViewById(R.id.btnDniDorso);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        finalizarBtn = findViewById(R.id.finalizarBtn);

        email = getIntent().getStringExtra("email");
        alias = getIntent().getStringExtra("alias");

        tabUsuario.setOnClickListener(v -> {
            esAlumno = false;
            contenedorAlumno.setVisibility(View.GONE);

            tabUsuario.setBackgroundTintList(getColorStateList(R.color.naranja));
            tabUsuario.setTextColor(getColor(R.color.white));

            tabAlumno.setBackgroundTintList(getColorStateList(R.color.beige_claro));
            tabAlumno.setTextColor(getColor(R.color.naranja));
        });

        tabAlumno.setOnClickListener(v -> {
            esAlumno = true;
            contenedorAlumno.setVisibility(View.VISIBLE);

            tabAlumno.setBackgroundTintList(getColorStateList(R.color.naranja));
            tabAlumno.setTextColor(getColor(R.color.white));

            tabUsuario.setBackgroundTintList(getColorStateList(R.color.beige_claro));
            tabUsuario.setTextColor(getColor(R.color.naranja));
        });

        fechaInput.setOnClickListener(v -> {
            final Calendar calendario = Calendar.getInstance();
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    RegisterCompleteActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        fechaInput.setText(fechaFormateada);
                    },
                    anio, mes, dia
            );
            datePicker.show();
        });

        finalizarBtn.setOnClickListener(v -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmar = confirmarPasswordInput.getText().toString().trim();
            String tramite = tramiteInput.getText().toString().trim();

            String fotoFrente = "base64_frente_simulado";
            String fotoDorso = "base64_dorso_simulado";

            if (nombre.isEmpty() || apellido.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmar)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!termsCheckbox.isChecked()) {
                Toast.makeText(this, "Debés aceptar los términos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esAlumno && tramite.isEmpty()) {
                Toast.makeText(this, "Falta el trámite del DNI", Toast.LENGTH_SHORT).show();
                return;
            }

            RegistroUsuarioRequest body = new RegistroUsuarioRequest(
                    email,
                    esAlumno,
                    nombre,
                    apellido,
                    password,
                    esAlumno ? fotoFrente : "",
                    esAlumno ? fotoDorso : "",
                    esAlumno ? tramite : "",
                    esAlumno ? "0.00" : ""
            );

            enviarRegistroFinal(body);
        });
    }

    private void enviarRegistroFinal(RegistroUsuarioRequest body) {
        ApiService apiService = RetrofitClient.getInstance().getApi();

        Call<ResponseBody> call = apiService.registrarUsuario(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterCompleteActivity.this, "Registro finalizado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterCompleteActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterCompleteActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterCompleteActivity.this, "❌ Sin conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
