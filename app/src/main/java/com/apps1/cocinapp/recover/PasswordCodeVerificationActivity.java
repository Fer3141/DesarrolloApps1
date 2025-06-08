package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.CodigoVerificacionRequest;
import com.apps1.cocinapp.register.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordCodeVerificationActivity extends AppCompatActivity {

    EditText codeInput;
    Button verifyButton;
    String email; // lo traemos desde el intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code_verification);

        codeInput = findViewById(R.id.codeInput);
        verifyButton = findViewById(R.id.verifyCodeButton);

        email = getIntent().getStringExtra("email");

        verifyButton.setOnClickListener(v -> {
            String codigo = codeInput.getText().toString().trim();

            // validamos que esté completo
            if (codigo.isEmpty()) {
                Toast.makeText(this, "completá el código", Toast.LENGTH_SHORT).show();
                return;
            }

            if (codigo.length() != 6) {
                Toast.makeText(this, "el código debe tener 6 dígitos", Toast.LENGTH_SHORT).show();
                return;
            }

            // armamos la llamada con retrofit
            ApiService apiService = RetrofitClient.getInstance().getApi();
            CodigoVerificacionRequest request = new CodigoVerificacionRequest(email, codigo);

            Call<ResponseBody> call = apiService.verificarCodigoRecuperacion(request);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordCodeVerificationActivity.this, "código validado", Toast.LENGTH_SHORT).show();

                        // pasamos al siguiente paso (reset de contraseña)
                        Intent intent = new Intent(PasswordCodeVerificationActivity.this, PasswordResetActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PasswordCodeVerificationActivity.this, "código incorrecto o vencido", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordCodeVerificationActivity.this, "falló conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
