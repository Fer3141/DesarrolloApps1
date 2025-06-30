package com.apps1.cocinapp.login;
import okhttp3.ResponseBody;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apps1.cocinapp.R;
import com.apps1.cocinapp.main.MainActivity;
import com.apps1.cocinapp.recover.PasswordRecoveryActivity;
import com.apps1.cocinapp.api.ApiService;
import com.apps1.cocinapp.register.LoginRequest;
import com.apps1.cocinapp.register.RegisterStartActivity;
import com.apps1.cocinapp.api.RetrofitClient;
import com.apps1.cocinapp.session.SharedPreferencesHelper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            ApiService api = RetrofitClient.getInstance().getApi();

            Call<ResponseBody> call = api.login(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject json = new JSONObject(responseBody);
                            String token = json.getString("token");

                            Log.d("LOGIN_DEBUG", "token recibido: " + token);


                            SharedPreferencesHelper.guardarToken(LoginActivity.this, token);

                            Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error al procesar el token", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try{
                            String errorMsg = response.errorBody().string();
                            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        findViewById(R.id.registerRedirectButton).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterStartActivity.class)));

        findViewById(R.id.forgotPassword).setOnClickListener(v ->
                startActivity(new Intent(this, PasswordRecoveryActivity.class)));
    }
}