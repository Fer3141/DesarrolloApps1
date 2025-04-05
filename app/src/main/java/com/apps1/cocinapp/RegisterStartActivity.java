package com.apps1.cocinapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

            // Simulamos que email y alias no existen
            String codigoGenerado = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Toast.makeText(this, "Código enviado: " + codigoGenerado, Toast.LENGTH_LONG).show();

            // Ir a la siguiente etapa del registro
           /* Intent intent = new Intent(this, RegisterCompleteActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("alias", alias);
            intent.putExtra("codigo", codigoGenerado);
            intent.putExtra("alumno", esAlumno);
            startActivity(intent);*/
        });
    }
}
