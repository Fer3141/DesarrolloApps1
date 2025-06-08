package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.EmailRequest;
import com.apps1.cocinapp.register.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecoveryActivity extends AppCompatActivity {

    EditText recoveryEmail;
    Button sendCodeButton, goBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        // vinculamos los elementos del xml
        recoveryEmail = findViewById(R.id.recoveryEmail);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        goBackToLogin = findViewById(R.id.goBackToLogin);

        // botón para enviar el código al mail
        sendCodeButton.setOnClickListener(v -> {
            String email = recoveryEmail.getText().toString().trim();

            // validamos que no esté vacío
            if (email.isEmpty()) {
                Toast.makeText(this, "completá el email", Toast.LENGTH_SHORT).show();
                return;
            }

            // validamos el formato de email
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "email inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            // armamos la llamada con retrofit usando EmailRequest
            ApiService apiService = RetrofitClient.getInstance().getApi();
            EmailRequest request = new EmailRequest(email);
            Call<ResponseBody> call = apiService.enviarCodigoRecuperacion(request);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordRecoveryActivity.this, "código enviado al mail", Toast.LENGTH_SHORT).show();

                        // pasamos al paso siguiente con el email
                        Intent intent = new Intent(PasswordRecoveryActivity.this, PasswordCodeVerificationActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else if (response.code() == 404) {
                        Toast.makeText(PasswordRecoveryActivity.this, "el correo no está registrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PasswordRecoveryActivity.this, "error al enviar el código", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordRecoveryActivity.this, "falló la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // botón para volver al login
        goBackToLogin.setOnClickListener(v -> finish());
    }
}
