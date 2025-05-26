package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

public class RegisterCodeActivity extends AppCompatActivity {

    EditText codigoInput;
    Button continuarBtn;
    String codigoEsperado, email, alias;
    boolean esAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);

        codigoInput = findViewById(R.id.codigoInput);
        continuarBtn = findViewById(R.id.continuarBtn);

        // Recuperar datos
        email = getIntent().getStringExtra("email");
        alias = getIntent().getStringExtra("alias");
        codigoEsperado = getIntent().getStringExtra("codigo");
        esAlumno = getIntent().getBooleanExtra("alumno", false);

        continuarBtn.setOnClickListener(v -> {
            String ingresado = codigoInput.getText().toString().trim();

            if (ingresado.equalsIgnoreCase(codigoEsperado)) {
                Intent intent = new Intent(this, RegisterCompleteActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("alias", alias);
                intent.putExtra("codigo", codigoEsperado);
                intent.putExtra("alumno", esAlumno);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
