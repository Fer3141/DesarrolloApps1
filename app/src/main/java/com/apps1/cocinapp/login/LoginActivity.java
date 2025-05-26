package com.apps1.cocinapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.recover.PasswordRecoveryActivity;
import com.apps1.cocinapp.register.RegisterStartActivity;

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
                if (email.equals("usuario") && password.equals("1234")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
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
