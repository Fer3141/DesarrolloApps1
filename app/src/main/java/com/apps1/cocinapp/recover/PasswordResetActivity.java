package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.register.ApiService;
import com.apps1.cocinapp.register.PasswordResetRequest;
import com.apps1.cocinapp.register.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetActivity extends AppCompatActivity {

    EditText newPasswordInput, confirmPasswordInput;
    Button resetPasswordButton;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // vinculamos los campos del xml
        newPasswordInput = findViewById(R.id.newPassword);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        resetPasswordButton = findViewById(R.id.resetPasswordButton); // asegurate del ID

        // recibimos el email desde la pantalla anterior
        email = getIntent().getStringExtra("email");

        resetPasswordButton.setOnClickListener(v -> {
            String password = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            // validaciones
            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "completá ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "la contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // llamamos al backend para guardar la nueva contraseña
            ApiService apiService = RetrofitClient.getInstance().getApi();
            PasswordResetRequest request = new PasswordResetRequest(email, password);

            apiService.resetPassword(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordResetActivity.this, "contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();

                        // volvemos al login
                        Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PasswordResetActivity.this, "no se pudo actualizar la contraseña", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PasswordResetActivity.this, "falló conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
