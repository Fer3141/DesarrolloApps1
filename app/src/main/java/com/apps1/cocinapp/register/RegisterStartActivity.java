package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import java.util.UUID;

public class RegisterStartActivity extends AppCompatActivity {

    EditText emailInput, aliasInput;
    CheckBox alumnoCheckbox;
    Button startRegistrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_start);

        emailInput = findViewById(R.id.emailInput);
        aliasInput = findViewById(R.id.aliasInput);
        alumnoCheckbox = findViewById(R.id.alumnoCheckbox);
        startRegistrationBtn = findViewById(R.id.startRegistrationBtn);

        startRegistrationBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String alias = aliasInput.getText().toString().trim();
            boolean esAlumno = alumnoCheckbox.isChecked();

            if (email.isEmpty() || alias.isEmpty()) {
                Toast.makeText(this, "Completá email y alias", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simular validación: el usuario NO existe (podés conectar a base real después)

            // Generar código
            String codigoGenerado = "1234";//UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Toast.makeText(this, "Código enviado a tu email: " + codigoGenerado, Toast.LENGTH_LONG).show();

            // Ir a pantalla de verificación de código
            Intent intent = new Intent(this, RegisterCodeActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("alias", alias);
            intent.putExtra("codigo", codigoGenerado);
            intent.putExtra("alumno", esAlumno);
            startActivity(intent);
        });
    }
}
