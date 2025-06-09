package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        startRegistrationBtn = findViewById(R.id.startRegistrationBtn);

        startRegistrationBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String alias = aliasInput.getText().toString().trim();

            if (email.isEmpty() || alias.isEmpty()) {
                Toast.makeText(this, "Completá email y alias", Toast.LENGTH_SHORT).show();
                return;
            }

            enviarDatosAlBackend(email, alias);
        });
    }

    private void enviarDatosAlBackend(String email, String alias) {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        RegistroRequest requestBody = new RegistroRequest(email, alias);

        Call<ResponseBody> call = apiService.registroInicial(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterStartActivity.this, "Código enviado al mail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterStartActivity.this, RegisterCodeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("alias", alias);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterStartActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterStartActivity.this, "❌ Sin conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
