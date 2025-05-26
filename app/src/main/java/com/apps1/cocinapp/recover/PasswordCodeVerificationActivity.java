package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

public class PasswordCodeVerificationActivity extends AppCompatActivity {

    String expectedCode = "123456"; // Simulación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code_verification);

        EditText codeInput = findViewById(R.id.codeInput);
        Button verifyButton = findViewById(R.id.verifyCodeButton);
        Button resendButton = findViewById(R.id.resendCodeButton);

        String email = getIntent().getStringExtra("email");

        verifyButton.setOnClickListener(v -> {
            String inputCode = codeInput.getText().toString().trim();

            if (inputCode.equals(expectedCode)) {
                Intent intent = new Intent(this, PasswordResetActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show();
            }
        });

        resendButton.setOnClickListener(v -> {
            Toast.makeText(this, "Código reenviado a " + email, Toast.LENGTH_SHORT).show();
            // Aquí podés implementar un reenvío real más adelante
        });
    }
}
