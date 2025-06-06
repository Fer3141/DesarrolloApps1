package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

            enviarDatosAlBackend(email, alias, esAlumno);
        });
    }

    private void enviarDatosAlBackend(String email, String alias, boolean esAlumno) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        RegistroRequest requestBody = new RegistroRequest(email, alias);

        Call<Void> call = apiService.verificarUsuario(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterStartActivity.this, "Código enviado al mail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterStartActivity.this, RegisterCodeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("alias", alias);
                    intent.putExtra("alumno", esAlumno);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterStartActivity.this, "Código HTTP: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterStartActivity.this, "❌ Sin conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}


