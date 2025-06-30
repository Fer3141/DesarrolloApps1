package com.apps1.cocinapp.recover;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.login.LoginActivity;
import com.apps1.cocinapp.register.PasswordResetRequest;
import com.apps1.cocinapp.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetActivity extends AppCompatActivity {

    EditText newPasswordInput, confirmPasswordInput;
    Button acceptButton;

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        newPasswordInput = findViewById(R.id.newPassword);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        acceptButton = findViewById(R.id.resetPasswordButton);

        // recibimos mail desde intent (código ya fue verificado antes)
        mail = getIntent().getStringExtra("mail");

        acceptButton.setOnClickListener(v -> {
            String nuevaPass = newPasswordInput.getText().toString().trim();
            String confirmarPass = confirmPasswordInput.getText().toString().trim();

            if (nuevaPass.isEmpty() || confirmarPass.isEmpty()) {
                Toast.makeText(this, "Completá ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!nuevaPass.equals(confirmarPass)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // se manda solo mail y pass (sin código)
            PasswordResetRequest body = new PasswordResetRequest(mail, nuevaPass, ""); // "" o null para código
            RetrofitClient.getInstance().getApi().resetPassword(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PasswordResetActivity.this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PasswordResetActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        try{
                            String errorMsg = response.errorBody().string();
                            Toast.makeText(PasswordResetActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(PasswordResetActivity.this, "Error inesperado: ", Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(PasswordResetActivity.this, "❌ " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
