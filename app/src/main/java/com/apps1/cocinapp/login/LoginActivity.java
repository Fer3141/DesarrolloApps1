
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
import com.apps1.cocinapp.register.LoginRequest;
import com.apps1.cocinapp.register.RegisterStartActivity;
import com.apps1.cocinapp.register.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // cuando se hace clic en "Iniciar sesión"
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // validación simple de campos vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // armar la request para enviar al backend
            LoginRequest request = new LoginRequest(email, password);

            ApiService api = RetrofitClient.getInstance().getApi();
            Call<usuarios> call = api.login(request);

            // se hace la llamada POST al backend
            call.enqueue(new Callback<usuarios>() {

            @Override
            public void onResponse(Call<usuarios> call, Response<usuarios> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarios usuario = response.body();

                    // ejemplo: mostrar nombre
                    Toast.makeText(LoginActivity.this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

                    // podés guardar usuario con SharedPreferences acá

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<usuarios> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
            });
        });

        // botón para registrarse
        Button registerRedirectButton = findViewById(R.id.registerRedirectButton);
        registerRedirectButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterStartActivity.class);
            startActivity(intent);
        });

        // botón para recuperar contraseña
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(intent);
        });
    }
}
