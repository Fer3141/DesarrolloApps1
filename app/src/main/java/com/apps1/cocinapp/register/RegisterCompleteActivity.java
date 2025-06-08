
package com.apps1.cocinapp.register;

import android.content.Intent;
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
import com.apps1.cocinapp.login.LoginActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCompleteActivity extends AppCompatActivity {

    EditText inputNombre, inputApellido, inputFecha, inputGenero, inputPassword, inputConfirmarPassword;
    EditText inputTramiteDni;
    LinearLayout contenedorAlumno;
    Button btnDniFrente, btnDniDorso, btnAgregarPago, finalizarBtn;
    Button tabUsuario, tabAlumno;
    CheckBox termsCheckbox;

    boolean esAlumno = false;
    String email, alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        inputNombre = findViewById(R.id.inputNombre);
        inputApellido = findViewById(R.id.inputApellido);
        inputFecha = findViewById(R.id.inputFecha);
        inputGenero = findViewById(R.id.inputGenero);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmarPassword = findViewById(R.id.inputConfirmarPassword);

        inputTramiteDni = findViewById(R.id.inputTramiteDni);
        contenedorAlumno = findViewById(R.id.contenedorAlumno);
        btnDniFrente = findViewById(R.id.btnDniFrente);
        btnDniDorso = findViewById(R.id.btnDniDorso);
        btnAgregarPago = findViewById(R.id.btnAgregarPago);

        termsCheckbox = findViewById(R.id.termsCheckbox);
        finalizarBtn = findViewById(R.id.finalizarBtn);
        tabUsuario = findViewById(R.id.tabUsuario);
        tabAlumno = findViewById(R.id.tabAlumno);

        contenedorAlumno.setVisibility(View.GONE);

        email = getIntent().getStringExtra("email");
        alias = getIntent().getStringExtra("alias");

        tabUsuario.setOnClickListener(v -> {
            esAlumno = false;
            contenedorAlumno.setVisibility(View.GONE);
            tabUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E68523")));
            tabUsuario.setTextColor(Color.WHITE);
            tabAlumno.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF5E9")));
            tabAlumno.setTextColor(Color.parseColor("#E68523"));
        });

        tabAlumno.setOnClickListener(v -> {
            esAlumno = true;
            contenedorAlumno.setVisibility(View.VISIBLE);
            tabAlumno.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E68523")));
            tabAlumno.setTextColor(Color.WHITE);
            tabUsuario.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF5E9")));
            tabUsuario.setTextColor(Color.parseColor("#E68523"));
        });

        finalizarBtn.setOnClickListener(v -> validarYRegistrar());
    }

    private void validarYRegistrar() {
        String nombre = inputNombre.getText().toString().trim();
        String apellido = inputApellido.getText().toString().trim();
        String fecha = inputFecha.getText().toString().trim();
        String genero = inputGenero.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmar = inputConfirmarPassword.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || fecha.isEmpty() || genero.isEmpty() ||
                password.isEmpty() || confirmar.isEmpty()) {
            Toast.makeText(this, "completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmar)) {
            Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsCheckbox.isChecked()) {
            Toast.makeText(this, "tenés que aceptar los términos", Toast.LENGTH_SHORT).show();
            return;
        }

        RegistroUsuarioRequest request = new RegistroUsuarioRequest();
        request.setMail(email);
        request.setAlias(alias);
        request.setPassword(password);
        request.setNombre(nombre);
        request.setApellido(apellido);
        request.setFechaNacimiento(fecha);
        request.setGenero(genero);
        request.setTipo(esAlumno ? "alumno" : "usuario");
        request.setHabilitado(true);

        if (esAlumno) {
            String tramiteDni = inputTramiteDni.getText().toString().trim();
            if (tramiteDni.isEmpty()) {
                Toast.makeText(this, "completá el número de trámite del DNI", Toast.LENGTH_SHORT).show();
                return;
            }
            request.setTramiteDni(tramiteDni);
            // request.setFotoDniFrente(...) // pendiente
            // request.setFotoDniDorso(...)  // pendiente
            // request.setMedioPago(...)     // pendiente
        }

        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<ResponseBody> call = apiService.registrarUsuario(request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterCompleteActivity.this, "registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterCompleteActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterCompleteActivity.this, "no se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterCompleteActivity.this, "falló conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
