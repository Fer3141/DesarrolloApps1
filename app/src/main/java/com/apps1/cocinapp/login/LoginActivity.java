package com.apps1.cocinapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.data.usuarios;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.recover.PasswordRecoveryActivity;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.RegisterStartActivity;
import com.apps1.cocinapp.register.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton, registerRedirectButton;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vincular vistas
        loginButton = findViewById(R.id.loginButton);
        registerRedirectButton = findViewById(R.id.registerRedirectButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        // BOTÓN LOGIN con validaciones + mock
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validación: campos vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación: formato de email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validación: longitud mínima
            if (password.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔗 Conexión simulada con Retrofit y Mocky
            ApiService apiService = RetrofitClient.getInstance().getApi();
            Call<usuarios> call = apiService.login();

            call.enqueue(new Callback<usuarios>() {
                @Override
                public void onResponse(Call<usuarios> call, Response<usuarios> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        usuarios usuario = response.body();
                        Toast.makeText(LoginActivity.this, "Login OK (mock): " + usuario.getAlias(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error login mock: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<usuarios> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Falló conexión mock: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // BOTÓN: Ir a registro
        registerRedirectButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterStartActivity.class);
            startActivity(intent);
        });

        // BOTÓN: Recuperar contraseña
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(intent);
        });
    }
}
