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

    EditText emailInput;
    Button sendCodeBtn, goBackToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        emailInput = findViewById(R.id.emailInput);
        sendCodeBtn = findViewById(R.id.sendCodeBtn);
        goBackToLoginBtn = findViewById(R.id.goBackToLogin);

        // Volver al login
        goBackToLoginBtn.setOnClickListener(v -> {
            finish(); // simplemente vuelve atrás
        });

        sendCodeBtn.setOnClickListener(v -> {
            String mail = emailInput.getText().toString().trim();

            if (mail.isEmpty()) {
                Toast.makeText(this, "Ingresá tu email", Toast.LENGTH_SHORT).show();
                return;
            }

            EmailRequest request = new EmailRequest(mail);
            ApiService api = RetrofitClient.getInstance().getApi();

            api.enviarCodigoRecuperacion(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordRecoveryActivity.this, "Código enviado al mail", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PasswordRecoveryActivity.this, PasswordCodeVerificationActivity.class);
                        intent.putExtra("mail", mail);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PasswordRecoveryActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordRecoveryActivity.this, "❌ " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
