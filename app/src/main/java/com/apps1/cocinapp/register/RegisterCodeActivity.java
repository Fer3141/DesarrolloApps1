package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCodeActivity extends AppCompatActivity {

    EditText codeInput;
    Button verifyCodeBtn;

    String email, alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);

        codeInput = findViewById(R.id.codigoInput);
        verifyCodeBtn = findViewById(R.id.continuarBtn); // <-- CORREGIDO

        email = getIntent().getStringExtra("email");
        alias = getIntent().getStringExtra("alias");

        verifyCodeBtn.setOnClickListener(v -> {
            String codigo = codeInput.getText().toString().trim();

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingresá el código", Toast.LENGTH_SHORT).show();
                return;
            }

            verificarCodigoConBackend(codigo);
        });
    }

    private void verificarCodigoConBackend(String codigo) {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        CodigoVerificacionRequest body = new CodigoVerificacionRequest(email, codigo);

        Call<ResponseBody> call = apiService.confirmarCodigo(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterCodeActivity.this, "Código correcto", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterCodeActivity.this, RegisterCompleteActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("alias", alias);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorMsg = response.errorBody().string();
                        Toast.makeText(RegisterCodeActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(RegisterCodeActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterCodeActivity.this, "❌ Sin conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
