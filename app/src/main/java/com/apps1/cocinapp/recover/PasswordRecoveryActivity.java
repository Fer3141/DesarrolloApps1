package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

public class PasswordRecoveryActivity extends AppCompatActivity {

    EditText recoveryEmail;
    Button sendCodeButton, goBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        recoveryEmail = findViewById(R.id.recoveryEmail);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        goBackToLogin = findViewById(R.id.goBackToLogin);

        sendCodeButton.setOnClickListener(v -> {
            String email = recoveryEmail.getText().toString().trim();

            // Simulación simple
            if (email.equals("usuario")) {
                Toast.makeText(this, "Código enviado al email", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, PasswordCodeVerificationActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Correo no registrado", Toast.LENGTH_SHORT).show();
            }
        });

        goBackToLogin.setOnClickListener(v -> {
            finish(); // vuelve al login
        });
    }
}
