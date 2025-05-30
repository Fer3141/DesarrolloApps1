package com.apps1.cocinapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps1.cocinapp.R;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.recover.PasswordRecoveryActivity;
import com.apps1.cocinapp.register.RegisterStartActivity;

import java.util.HashMap;
import java.util.Map;

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

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                String url = "https://run.mocky.io/v3/4f2540fe-9007-4de6-8db8-850c1baf0e0a"; // REEMPLAZAR por la URL de la api

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        response -> {
                            // Si el servidor responde OK (puede venir JSON, esto es de ejemplo)
                            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        },
                        error -> {
                            Toast.makeText(LoginActivity.this, "Error en el login", Toast.LENGTH_SHORT).show();
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };

                queue.add(request);
            }
        });

        Button registerRedirectButton = findViewById(R.id.registerRedirectButton);
        registerRedirectButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterStartActivity.class);
            startActivity(intent);
        });

        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(intent);
        });


    }
}
