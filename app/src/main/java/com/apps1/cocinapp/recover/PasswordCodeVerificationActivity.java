package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Button verifyButton, resendButton;
    TextView noRecibisteCodigo;
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_code_verification);

        codeInput = findViewById(R.id.codeInput);
        verifyButton = findViewById(R.id.verifyButton);
        resendButton = findViewById(R.id.resendButton);
        noRecibisteCodigo = findViewById(R.id.noRecibisteCodigo);

        mail = getIntent().getStringExtra("mail");

        verifyButton.setOnClickListener(v -> {
            String codigo = codeInput.getText().toString().trim();

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Completá el código", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getInstance().getApi();
            CodigoVerificacionRequest request = new CodigoVerificacionRequest(mail, codigo);

            apiService.verificarCodigoRecuperacion(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordCodeVerificationActivity.this, "Código validado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PasswordCodeVerificationActivity.this, PasswordResetActivity.class);
                        intent.putExtra("mail", mail);
                        intent.putExtra("codigo", codigo); // lo pasamos también
                        startActivity(intent);
                        finish();
                    } else {
                        try{
                            String errorMsg = response.errorBody().string();
                            Toast.makeText(PasswordCodeVerificationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(PasswordCodeVerificationActivity.this, "Error inesperado: ", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordCodeVerificationActivity.this, "❌ Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        resendButton.setOnClickListener(v -> {
            ApiService api = RetrofitClient.getInstance().getApi();
            api.enviarCodigoRecuperacion(new com.apps1.cocinapp.register.EmailRequest(mail)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordCodeVerificationActivity.this, "Código reenviado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PasswordCodeVerificationActivity.this, "No se pudo reenviar", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordCodeVerificationActivity.this, "❌ Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
