package com.apps1.cocinapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.RetrofitClient;
import com.apps1.cocinapp.register.CodigoVerificacionRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCodeActivity extends AppCompatActivity {

    EditText codigoInput;
    Button continuarBtn;
    String email, alias;
    boolean esAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);

        codigoInput = findViewById(R.id.codigoInput);
        continuarBtn = findViewById(R.id.continuarBtn);

        email = getIntent().getStringExtra("email");
        alias = getIntent().getStringExtra("alias");
        esAlumno = getIntent().getBooleanExtra("alumno", false);

        continuarBtn.setOnClickListener(v -> {
            String codigo = codigoInput.getText().toString().trim();

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Ingresá el código", Toast.LENGTH_SHORT).show();
                return;
            }

            verificarCodigoConBackend(email, codigo);
        });
    }

    private void verificarCodigoConBackend(String email, String codigo) {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        CodigoVerificacionRequest request = new CodigoVerificacionRequest(email, codigo);

        Call<ResponseBody> call = apiService.verificarCodigo(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterCodeActivity.this, "✅ Verificación exitosa", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterCodeActivity.this, RegisterCompleteActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("alias", alias);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterCodeActivity.this, "❌ Código incorrecto o expirado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterCodeActivity.this, "⚠️ Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
